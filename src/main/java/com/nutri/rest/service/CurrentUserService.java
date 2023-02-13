package com.nutri.rest.service;

import com.nutri.rest.exception.ValidationException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
public class CurrentUserService {

  public String getLoggedUserName() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow(() -> new ValidationException("No logged in user"));
  }

}