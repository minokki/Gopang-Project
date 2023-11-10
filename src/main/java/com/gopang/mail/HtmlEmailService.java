package com.gopang.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class HtmlEmailService implements EmailService{

    private final JavaMailSender javaMailSender;

    /* 이메일 SEND */
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8"); //첨부파일 보내려면 true
            messageHelper.setTo(emailMessage.getTo());
            messageHelper.setSubject(emailMessage.getSubject());
            messageHelper.setText(emailMessage.getMessage(),true);
            javaMailSender.send(mimeMessage);
            log.info("sent email: {} " , emailMessage.getMessage());
        } catch (MessagingException e) {
            log.error("failed to send email",e);
        }
    }
}
