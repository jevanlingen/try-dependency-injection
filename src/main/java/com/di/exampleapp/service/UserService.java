package com.di.exampleapp.service;

import com.di.annotations.Service;
import com.di.exampleapp.repository.UserRepository;
import com.di.model.User;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUser(int id) {
        return userRepository.findById(id);
    }

    public User createUser(String name) {
        User newUser = new User(null, name);
        userRepository.save(newUser);
        return newUser;
    }
}
