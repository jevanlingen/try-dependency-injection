package com.di.model.events.audit;

public class Read extends AuditEvent {
    public Read(String message) {
        super(message);
    }
}
