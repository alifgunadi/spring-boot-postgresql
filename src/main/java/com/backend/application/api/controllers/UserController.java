package com.backend.application.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.application.api.exceptions.ResourceNotFoundException;
import com.backend.application.api.models.User;
import com.backend.application.api.repositories.UserRepo;

@RestController
public class UserController {

    @Autowired
    public UserRepo userRepo;

    @GetMapping(value = "/list/users")
    public ResponseEntity<List<User>> getUsers() {

        List<User> users = userRepo.findAll();

        for (User user : users) {
            user.setPassword(null);
        }

        return ResponseEntity.ok(users);

    }

    @GetMapping(value = "/detail/users/{id}")
    public ResponseEntity<User> detailUser(@PathVariable long id) {

        User userOptional = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found:" + id));

        userOptional.setPassword(null);

        return ResponseEntity.ok(userOptional);

    }

    @PostMapping(value = "/register/user")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        userRepo.save(user);

        return ResponseEntity.ok("Register is successfully.");
    }

}
