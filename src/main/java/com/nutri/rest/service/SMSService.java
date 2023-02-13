package com.nutri.rest.service;

import com.nutri.rest.model.Message;
import com.nutri.rest.request.SMSRequestRoot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class SMSService {

   private final RestTemplate restTemplate;

    @Value("${sms.url}")
    private String url;

    public SMSService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendSMS(String mobileNumber,String text){

        List<String> destNumbers=new ArrayList<>();
        destNumbers.add(mobileNumber);
        Message message= Message.
                builder()
                .dest(destNumbers)
                .send("NutriEatsSMS")
                .type("PM")
                .text(text)
                .build();
        List<Message> messagesList=new ArrayList<>();
        messagesList.add(message);

        SimpleDateFormat df=new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND,4);
        System.out.println("Date : "+df.format(c.getTime()));

        SMSRequestRoot smsRequestRoot = SMSRequestRoot.builder()
                .encrpt("0")
                .key("RysSr0sozpUpzLRyS7dCMg==")
                //.sch_at(df.format(c.getTime()))
                .messages(messagesList)
                .ver("1.0")
                .build();




     String responseRoot=   restTemplate.postForObject(url, smsRequestRoot, String.class);

        System.out.println(responseRoot);

    if(responseRoot.contains("200")){
         return "Success";
     }else{
         return "Failure";
     }
    }

}
