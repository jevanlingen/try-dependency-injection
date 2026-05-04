package com.di.exampleapp.service;

import com.di.annotations.EventListener;
import com.di.annotations.Service;
import com.di.architecture.EventBus;
import com.di.model.ExampleEvent;
import com.di.model.ExampleEvent2;

@Service
public class FirstEventListenerService {
    private final EventBus eventBus;

    public FirstEventListenerService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @EventListener
    public void onEvent(ExampleEvent event) {
        System.out.println("FirstEventListenerService received: " + event.data());

        try {
            Thread.sleep((long) (Math.random() * 1000));

            final var newEvent = Math.random() < 0.5
                    ? new ExampleEvent("Event 1 data at " + System.currentTimeMillis())
                    : new ExampleEvent2("Event 2 data at " + System.currentTimeMillis());
            eventBus.publish(newEvent);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
