package com.museum.auth.services;

import com.museum.auth.domain.aggregate.UserId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;
    private final TelegramService telegramService;
    private final NotificationService notificationService;
    private final JwtTokenService jwtTokenService;

    public TelegramBot(@Value("${telegram.bot.token}") String botToken,
                       @Value("${telegram.bot.name}") String botName,
                       TelegramService telegramService,
                       NotificationService notificationService,
                       JwtTokenService jwtTokenService) {
        super(botToken);
        this.botName = botName;
        this.telegramService = telegramService;
        this.notificationService = notificationService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/start user_")) {
                handleStartCommand(messageText, chatId);
            }
        }
    }

    private void handleStartCommand(String messageText, long chatId) {
        String systemToken = jwtTokenService.generateSystemAdminToken();
        try {
            String userIdStr = messageText.replace("/start user_", "");
            Integer userId = Integer.parseInt(userIdStr);

            telegramService.linkTelegram(userId, chatId);

            notificationService.sendTelegramNotification("chat:" + chatId, "✅ Your account has been successfully linked to the Museum Microservice!", systemToken);
        } catch (NumberFormatException e) {
            notificationService.sendTelegramNotification("chat:" + chatId, "❌ Invalid link format. Please use the link provided in the application.", systemToken);
        } catch (Exception e) {
            notificationService.sendTelegramNotification("chat:" + chatId, "❌ Error linking account: " + e.getMessage(), systemToken);
            throw e;
        }
    }

    public void sendNotification(long chatId, String text) {
        String systemToken = jwtTokenService.generateSystemAdminToken();
        notificationService.sendTelegramNotification("chat:" + chatId, text, systemToken);
    }
}
