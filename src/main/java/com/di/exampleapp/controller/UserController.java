package com.di.exampleapp.controller;

import com.di.annotations.RestController;
import com.di.annotations.http.GET;
import com.di.annotations.http.POST;
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

    @GET("/user/1")
    public String getUser() {
        return "User with ID 1";
    }

    @POST("/user")
    public String createUser() {
        return "User created";
    }
}
