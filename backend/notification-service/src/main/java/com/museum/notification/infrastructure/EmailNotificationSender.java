package com.museum.notification.infrastructure;

import com.museum.notification.domain.aggregate.NotificationChannel;
import com.museum.notification.domain.contracts.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${test.email.override:}")
    private String testEmailOverride;

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.EMAIL;
    }

    @Override
    public void send(String target, String message) {
        String finalTarget = (testEmailOverride != null && !testEmailOverride.isBlank()) 
                ? testEmailOverride 
                : target;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(finalTarget);
        mailMessage.setSubject("Museum Notification");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
