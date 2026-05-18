package com.di.model.events;

import com.di.model.User;

public record UserCreated(User user) implements Event {
}
