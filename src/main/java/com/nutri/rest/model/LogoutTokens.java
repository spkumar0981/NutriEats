package com.nutri.rest.model;

import com.nutri.rest.config.AuditableEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Table(name = "LOGOUT_SESSION")
public class LogoutTokens extends AuditableEntity<String> {
    @Id
    private String token;
    private String userName;
    private Date tokenExpiryTime;
    
}
