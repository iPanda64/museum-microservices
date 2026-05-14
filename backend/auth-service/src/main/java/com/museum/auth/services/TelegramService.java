package com.museum.auth.services;

import com.museum.auth.domain.aggregate.Credentials;
import com.museum.auth.domain.aggregate.UserId;
import com.museum.auth.domain.daocontracts.CredentialsDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TelegramService {

    private final CredentialsDAO credentialsDAO;
    private final NotificationService notificationService;

    @Value("${telegram.bot.name}")
    private String botName;

    public String getTelegramLink(Integer userId) {
        return "https://t.me/" + botName + "?start=user_" + userId;
    }

    public boolean isTelegramLinked(Integer userId) {
        return credentialsDAO.findById(new UserId(userId))
                .map(c -> c.telegramChatId() != null)
                .orElse(false);
    }

    public void linkTelegram(Integer userId, Long telegramChatId) {
        Credentials credentials = credentialsDAO.findById(new UserId(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        credentialsDAO.save(credentials.linkTelegram(telegramChatId));
    }

    public Long getTelegramChatId(Integer userId) {
        return credentialsDAO.findById(new UserId(userId))
                .map(Credentials::telegramChatId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void unlinkTelegram(Integer userId) {
        Credentials credentials = credentialsDAO.findById(new UserId(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (credentials.telegramChatId() != null) {
            notificationService.sendTelegramNotification(userId.toString(), 
                "⚠️ Your account has been unlinked from the Museum Microservice.");
        }
        
        credentialsDAO.save(credentials.unlinkTelegram());
    }
}
