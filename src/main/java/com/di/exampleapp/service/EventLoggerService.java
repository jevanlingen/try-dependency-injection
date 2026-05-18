package com.di.exampleapp.service;

import com.di.annotations.EventListener;
import com.di.annotations.Service;
import com.di.model.events.Event;

import java.time.LocalDateTime;

@Service
public class EventLoggerService {
    @EventListener
    public void onEvent(Event event) {
        System.out.println("Event: " + event);
    }
}
