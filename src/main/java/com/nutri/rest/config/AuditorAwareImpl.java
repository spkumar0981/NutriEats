package com.nutri.rest.config;

import com.nutri.rest.service.CurrentUserService;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
       return Optional.ofNullable(CurrentUserService.getLoggedUserName());
    }
}