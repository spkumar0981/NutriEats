package com.nutri.rest.mapper;

import com.nutri.rest.model.AuditLogging;
import com.nutri.rest.request.LoginRequest;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class AuditLoggingMapper {

    public AuditLogging mapAuditLogging(LoginRequest loginRequest, String token, String ip){
        return AuditLogging.builder()
                .ip(ip)
                .loginTime(new Date())
                .userName(loginRequest.getUserName())
                .token(token).build();
    }
}
