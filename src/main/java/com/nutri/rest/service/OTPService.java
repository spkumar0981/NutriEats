package com.nutri.rest.service;

import com.nutri.rest.exception.ValidationException;
import com.nutri.rest.model.OTP;
import com.nutri.rest.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OTPService {

    private final OTPRepository otpRepository;
    private final SMSService smsService;

    @Value("${spring.datasource.driver-class-name}")
    String driverName;

    public OTPService(OTPRepository otpRepository, SMSService smsService) {
        this.otpRepository = otpRepository;
        this.smsService = smsService;
    }

    public String generateOTP(String userName,String mobileNumber){
        LocalDateTime timeBefore10Mins = LocalDateTime.now().minusMinutes(10);
        /*List<OTP> optionalOTP = otpRepository.findByUserNameAndDateBetween(userName, timeBefore10Mins, LocalDateTime.now());
        if((!optionalOTP.isEmpty()) && optionalOTP.size()>3)
            throw new ValidationException("OTP sending failure ! You have exceeded the OTP limit! please try again after 10 mins");*/

        Optional<OTP> optionalOTP = otpRepository.findByUserName(userName);

        int otpCount = optionalOTP.isPresent()?optionalOTP.get().getOtpCount():0;
        LocalDateTime createDate = optionalOTP.isPresent()?optionalOTP.get().getCreatedDate() : LocalDateTime.now();

        if(otpCount>0 && timeBefore10Mins.isAfter(optionalOTP.get().getCreatedDate())){
            otpCount =0;
            createDate = LocalDateTime.now();
        }

        if(otpCount>3)
            throw new ValidationException("OTP sending failure ! You have exceeded the OTP limit! please try again after 10 mins");




        SecureRandom random = new SecureRandom();
        byte sessBytes[] = new byte[32];
        random.nextBytes(sessBytes);
//        String sessionId = new String(sessBytes);
        int randInRange = random.nextInt(999999);
        String otp= new DecimalFormat("000000").format(randInRange);

        otpRepository.save(OTP.builder().userName(userName)
                .otp(otp)
                .createdDate(createDate)
                .expiryTime(LocalDateTime.now().plusDays(1))
                .otpCount(otpCount+1)
                .build());
        String text="Use OTP <OTP Number> for Nutri Eats application password change/reset. Valid for the day.";
        text=text.replace("<OTP Number>",otp);
        //String text="OTP for reset password - "+otp+" valid for 3 minutes!";

        if(!("org.h2.Driver".equals(driverName))) {
            String smsStatus = smsService.sendSMS(mobileNumber, text);
            if (smsStatus.equals("Success")) {
                return "OTP Sent to Registered Mobile";
            } else {
                throw new ValidationException("OTP sending failure ! please try again after sometime!");
            }
        }else
            return "Success";

    }

    public OTP findByUserName(String userName){
      Optional<OTP> optionalOTP = otpRepository.findByUserName(userName);
      if(optionalOTP.isPresent()){
          OTP otp=optionalOTP.get();
          LocalDateTime currentDateTime=LocalDateTime.now();
          if(otp.getExpiryTime().isBefore(LocalDateTime.now())){
                throw new ValidationException("OTP Expired");
          }
          return otp;
      }else{
          throw new ValidationException("Invalid request");
      }
    }
}
