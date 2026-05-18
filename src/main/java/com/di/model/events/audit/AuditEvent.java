package com.di.model.events.audit;

import com.di.model.events.Event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class AuditEvent implements Event {
    private final String message;
    private final LocalDateTime created;

    public AuditEvent(String message) {
        this.message = message;
        this.created = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public String message() {
        return message;
    }

    public LocalDateTime when() {
        return created;
    }

    @Override
    public String toString() {
        return "AuditEvent[" + "message='" + message + '\'' + ", created=" + created + ']';
    }
}
