package com.nutri.rest.service;

import com.nutri.rest.exception.EmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
      helper = new MimeMessageHelper(message, true,"UTF-8");
      helper.setTo(email);
      helper.setFrom(supportEmail);
      helper.setSubject(subject);
      helper.setText(body, true);
      mailSender.send(message);
    } catch (MessagingException e) {
      throw new EmailException("Sending email for reset password failed for email : " + email);
    }
    return "success";
  }

  public String sendOrderConfirmationMailToCustomer(String email, String customerName, String restaurantName, String orderNumber) {
    String subject = "Nutri Eats order confirmation";
    String body = "<html><body><img src='cid:myLogo' style='display:block;margin:auto;'><br/><hr><br/>" +
            "Hi "+customerName+",<br/><br/>" +
            "Thanks for using Nutri Eats! <br/>Your order from <b>"+restaurantName+"</b> has been confirmed.<br/><br/><br/>" +
            "Order No: <b>#ORDER-"+orderNumber+"</b><br/>" +
            "Restaurant: <b>"+restaurantName+"</b></body></html>";
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name());
      helper.setTo(email);
      helper.setFrom(supportEmail);
      helper.setSubject(subject);

      Resource resource = new ClassPathResource("newlogo.png");
      File attachment = resource.getFile();
      FileSystemResource file = new FileSystemResource(attachment);

      helper.setText(body, true);
      helper.addInline("myLogo", file);
      mailSender.send(message);
    } catch (MessagingException | IOException e) {
      throw new EmailException("Sending email for sendOrderConfirmationMailToCustomer failed for email : " + email);
    }
    return "success";
  }

  public String sendOrderDeliveredMailToCustomer(String email, String customerName, String restaurantName, String orderNumber) {
    String subject = "Your Nutri Eats order has been delivered";
    String body = "<html><body><img src='cid:myLogo' style='display:block;margin:auto;'><br/><hr><br/>" +
            "Hi "+customerName+",<br/><br/>" +
            "Thanks for using Nutri Eats! <br/>Your order from <b>"+restaurantName+"</b> has been delivered.<br/><br/><br/>" +
            "Order No: <b>#ORDER-"+orderNumber+"</b><br/>" +
            "Restaurant: <b>"+restaurantName+"</b></body></html>";
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name());
      helper.setTo(email);
      helper.setFrom(supportEmail);
      helper.setSubject(subject);

      Resource resource = new ClassPathResource("newlogo.png");
      File attachment = resource.getFile();
      FileSystemResource file = new FileSystemResource(attachment);

      helper.setText(body, true);
      helper.addInline("myLogo", file);
      mailSender.send(message);
    } catch (MessagingException e) {
      throw new EmailException("Sending email for sendOrderDeliveredMailToCustomer failed for email : " + email);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return "success";
  }
}
