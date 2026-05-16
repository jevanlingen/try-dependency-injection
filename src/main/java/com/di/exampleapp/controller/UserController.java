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
        return userService.getAll();
    }

    @GET("/user/1")
    public User getUser() {
        return userService.getUser(1);
    }

    @POST("/user")
    public User createUser() {
        return userService.createUser("New User");
    }
}
