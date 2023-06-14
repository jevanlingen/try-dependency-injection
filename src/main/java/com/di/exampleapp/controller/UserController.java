package com.di.exampleapp.controller;

import com.di.annotations.RestController;
import com.di.exampleapp.service.AuthenticationService;
import com.di.exampleapp.service.UserService;

@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(final UserService userService, final AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }
}
