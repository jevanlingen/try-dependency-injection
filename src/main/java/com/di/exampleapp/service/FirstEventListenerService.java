package com.di.exampleapp.service;

import com.di.annotations.EventListener;
import com.di.annotations.Service;
import com.di.architecture.EventBus;
import com.di.model.*;

@Service
public class FirstEventListenerService {
    private final EventBus eventBus;

    public FirstEventListenerService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @EventListener
    public void onEvent(DataEvent event) {
        System.out.println("FirstEventListenerService received: " + event.data());
        sendNextEvent();
    }

    @EventListener
    public void onEvent2(AnotherEvent event) {
        System.out.println("FirstEventListenerService received: " + event.a() + " & " + event.b());
        sendNextEvent();
    }

    private void sendNextEvent() {
        try {
            Thread.sleep((long) (Math.random() * 1000));

            final var x = Math.random();
            final Event newEvent;
            if (x < 0.33) {
                newEvent = new DataEvent("Data Event at " + System.currentTimeMillis());
            } else if (x < 0.66) {
                newEvent = new MessageEvent("MessageEvent at " + System.currentTimeMillis());
            } else {
                newEvent = new AnotherEvent("A", "B");
            }

            eventBus.publish(newEvent);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
