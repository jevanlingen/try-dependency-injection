package com.di.exampleapp.service;

import com.di.annotations.EventListener;
import com.di.annotations.Service;
import com.di.architecture.EventBus;
import com.di.model.DataEvent;
import com.di.model.MessageEvent;

@Service
public class SecondEventListenerService {
    private final EventBus eventBus;

    public SecondEventListenerService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @EventListener
    public void handle(MessageEvent event) {
        System.out.println("SecondEventListenerService received MessageEvent: " + event.message());

        try {
            Thread.sleep((long) (Math.random() * 1000));
            eventBus.publish(new DataEvent("Data Event at " + System.currentTimeMillis()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
