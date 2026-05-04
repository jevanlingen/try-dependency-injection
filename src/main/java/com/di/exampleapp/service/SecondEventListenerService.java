package com.di.exampleapp.service;

import com.di.annotations.EventListener;
import com.di.annotations.Service;
import com.di.architecture.EventBus;
import com.di.model.ExampleEvent;
import com.di.model.ExampleEvent2;

@Service
public class SecondEventListenerService {
    private final EventBus eventBus;

    public SecondEventListenerService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @EventListener
    public void handle(ExampleEvent2 event) {
        System.out.println("SecondEventListenerService received ExampleEvent2: " + event.message());

        try {
            Thread.sleep((long) (Math.random() * 1000));
            eventBus.publish(new ExampleEvent("Event 1 data at " + System.currentTimeMillis()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
