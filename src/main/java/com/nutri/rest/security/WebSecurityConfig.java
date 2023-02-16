package com.nutri.rest.security;

import com.nutri.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userDetailsService;

    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    @Value("${spring.datasource.driver-class-name}")
    String driverName;

    public WebSecurityConfig() {

    }

    public WebSecurityConfig(boolean disableDefaults, UserService userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        super(disableDefaults);
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {

        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] AUTH_WHITELIST = {

            // -- swagger ui
            "/swagger-resources/**",
            "/h2/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/api/v1/download/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable();
        /*.and().headers()
                .hsts().disable()
                .frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN);*/
        if(!("org.h2.Driver".equals(driverName))) {
            http
                    .headers()
                    .contentSecurityPolicy("default-src 'self'");
            http.headers()
                    .httpStrictTransportSecurity()
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000);
            http.headers()
                    .xssProtection()
                    .block(false);
            http.headers()
                    .addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy","default-src 'self'"))
                    .addHeaderWriter(new StaticHeadersWriter("X-WebKit-CSP","default-src 'self'"));
            http.headers()
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.DENY));
        }
        http.cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST)
                .permitAll()
                .antMatchers("/api/v1/users/signIn/**")
                .permitAll()
                .antMatchers("/api/v1/users/createUser")
                .permitAll()
                .antMatchers("/api/v1/users/captcha/**")
                .permitAll()
                .antMatchers("/api/v1/users/encryptPassword")
                .permitAll()
                .antMatchers("/api/v1/users/resetPassword/**")
                .permitAll()
                .antMatchers("/api/v1/notifications/**")
                .permitAll()
                .antMatchers("/*")
                .permitAll()
                .antMatchers("/h2/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(
                authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}