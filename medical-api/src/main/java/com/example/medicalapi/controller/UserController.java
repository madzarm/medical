package com.example.medicalapi.controller;

import com.example.medicalapi.service.UserService;
import com.example.medicalapi.service.request.RegisterUserRequest;
import com.example.medicalapi.service.result.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ActionResult> registerUser(@Valid RegisterUserRequest request){
        return userService.registerUser(request).intoResponseEntity();

    }
}
