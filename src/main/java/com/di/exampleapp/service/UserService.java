package com.di.exampleapp.service;

import com.di.annotations.Service;
import com.di.architecture.EventBus;
import com.di.exampleapp.repository.UserRepository;
import com.di.model.User;
import com.di.model.events.UserCreated;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EventBus eventBus;

    public UserService(final UserRepository userRepository, EventBus eventBus) {
        this.userRepository = userRepository;
        this.eventBus = eventBus;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUser(int id) {
        return userRepository.findById(id);
    }

    public User createUser(String name) {
        final var newUser = userRepository.save(new User(null, name));
        eventBus.publish(new UserCreated(newUser));

        return newUser;
    }
}
