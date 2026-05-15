package com.museum.notification.domain.iterators;

import com.museum.notification.domain.aggregate.NotificationChannel;
import com.museum.notification.domain.contracts.NotificationSender;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SenderIterator implements Iterator<NotificationSender> {
    private final List<NotificationSender> senders;
    private final NotificationChannel channel;
    private int currentIndex = 0;

    public SenderIterator(List<NotificationSender> senders, NotificationChannel channel) {
        this.senders = senders;
        this.channel = channel;
    }

    @Override
    public boolean hasNext() {
        while (currentIndex < senders.size()) {
            if (senders.get(currentIndex).supports(channel)) {
                return true;
            }
            currentIndex++;
        }
        return false;
    }

    @Override
    public NotificationSender next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No sender found for channel: " + channel);
        }
        return senders.get(currentIndex++);
    }
}
