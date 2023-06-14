package com.di.exampleapp.service;

import com.di.annotations.Service;
import com.di.exampleapp.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
