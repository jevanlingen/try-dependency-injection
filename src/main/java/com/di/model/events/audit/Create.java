package com.di.model.events.audit;

public class Create extends AuditEvent {
    public Create(String message) {
        super(message);
    }
}
