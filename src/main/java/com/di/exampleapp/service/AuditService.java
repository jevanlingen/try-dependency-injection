package com.di.exampleapp.service;

import com.di.annotations.EventListener;
import com.di.annotations.Service;
import com.di.model.events.audit.AuditEvent;

@Service
public class AuditService {
    @EventListener
    public void onAuditEvent(AuditEvent event) {
        System.out.println(event.when() + " " + event.getClass().getSimpleName().toUpperCase() + ": " + event.message());
    }
}
