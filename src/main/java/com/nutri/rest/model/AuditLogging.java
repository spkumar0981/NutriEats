package com.nutri.rest.model;

import com.nutri.rest.config.AuditableEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Table(name = "AUDIT_LOGGING")
public class AuditLogging extends AuditableEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    private String ip;
    private Date loginTime;
    private Date logoutTime;
    private String token;
}
