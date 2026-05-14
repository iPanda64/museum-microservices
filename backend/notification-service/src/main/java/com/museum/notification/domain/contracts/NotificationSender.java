package com.museum.notification.domain.contracts;

import com.museum.notification.domain.aggregate.NotificationChannel;

public interface NotificationSender {
    boolean supports(NotificationChannel channel);
    void send(String target, String message);
}
