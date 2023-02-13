package com.nutri.rest.service;

import com.nutri.rest.exception.EmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

  private final JavaMailSender mailSender;

  @Value("${support.email}")
  private String supportEmail;

    @Value("${reset.email.base.url}")
    private String resetBaseUrl;

  public MailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public String sendResetMail(String email, String token) {
    String subject = "Nutri Eats password reset";
    String body = "Please click the link below to reset password \n" + resetBaseUrl + token;
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(message, true);
      helper.setTo(email);
      helper.setFrom(supportEmail);
      helper.setSubject(subject);
      helper.setText(body);
      mailSender.send(message);
    } catch (MessagingException e) {
      throw new EmailException("Sending email for reset password failed for email : " + email);
    }
  return "success";
  }
}
