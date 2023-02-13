package com.nutri.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        ParameterBuilder paramBuilder = new ParameterBuilder();
        paramBuilder
                .name("Authorization")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        List<Parameter> params =
                new ArrayList<Parameter>();
        params.add(paramBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("All Services")
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nutri.rest.controller"))
                .build()
                .globalOperationParameters(params);
    }

    // TODO: Check the proper version of Apache License for closed source and update the right version
    // and URL of license info.
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Nutri Eats API")
                .description("Nutri Eats API for Services")
                .license("Apache License Version 2.0")
                .licenseUrl("")
                .version("2.0")
                .build();
    }
}
