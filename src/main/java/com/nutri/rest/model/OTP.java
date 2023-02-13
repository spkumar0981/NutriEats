package com.nutri.rest.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class OTP {

    @Id
    private String userName;
    private String otp;
    private LocalDateTime createdDate;
    private LocalDateTime expiryTime;
    private int otpCount;
}
