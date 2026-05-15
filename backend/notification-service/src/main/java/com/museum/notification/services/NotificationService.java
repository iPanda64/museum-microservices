package com.museum.notification.services;

import com.museum.notification.domain.aggregate.NotificationChannel;
import com.museum.notification.domain.contracts.NotificationSender;
import com.museum.notification.domain.iterators.SenderIterator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final List<NotificationSender> senders;

    public void sendNotification(NotificationChannel channel, String target, String message) {
        SenderIterator iterator = new SenderIterator(senders, channel);
        
        if (iterator.hasNext()) {
            NotificationSender sender = iterator.next();
            sender.send(target, message);
        } else {
            throw new IllegalArgumentException("Unsupported notification channel: " + channel);
        }
    }
}
