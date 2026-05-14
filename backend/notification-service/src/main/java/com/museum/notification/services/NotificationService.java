package com.museum.notification.services;

import com.museum.notification.domain.aggregate.NotificationChannel;
import com.museum.notification.domain.contracts.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final List<NotificationSender> senders;

    public void sendNotification(NotificationChannel channel, String target, String message) {
        senders.stream()
                .filter(sender -> sender.supports(channel))
                .findFirst()
                .ifPresentOrElse(
                        sender -> sender.send(target, message),
                        () -> { throw new IllegalArgumentException("Unsupported notification channel: " + channel); }
                );
    }
}
