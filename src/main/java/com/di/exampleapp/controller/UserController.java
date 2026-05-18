package com.di.exampleapp.controller;

import com.di.annotations.RestController;
import com.di.annotations.http.GET;
import com.di.annotations.http.POST;
import com.di.exampleapp.service.AuthenticationService;
import com.di.exampleapp.service.UserService;
import com.di.model.User;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(final UserService userService, final AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GET("/user")
    public List<User> getUsers() {
        // audit event
        return userService.getAll();
    }

    @GET("/user/{id}")
    public User getUser(int id) {
        // audit event
        return userService.getUser(id);
    }

    @POST("/user")
    public User createUser() {
        // audit event
        return userService.createUser("New User");
    }
}
