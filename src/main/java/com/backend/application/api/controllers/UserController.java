package com.backend.application.api.controllers;

import java.util.List;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.application.api.exceptions.ResourceNotFoundException;
import com.backend.application.api.models.User;
import com.backend.application.api.repositories.UserRepo;

@RestController
public class UserController {

    @Autowired
    public UserRepo userRepo;
    private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();

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

    @PostMapping(value = "/register/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@ModelAttribute User user, @RequestParam("file") MultipartFile file) {

        String encryptPassword = bcryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encryptPassword);

        userRepo.save(user);

        return ResponseEntity.ok("Register is successfully.");
    }

    @PostMapping(value = "/update/user")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        long id = user.getId();
        User userId = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id not found:" + id));

        if (user.getPassword() != "" && user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encryptPassword = bcryptPasswordEncoder.encode(user.getPassword());
            userId.setPassword(encryptPassword);
        }

        userId.setUsername(user.getUsername());
        userId.setEmail(user.getEmail());
        userId.setPrivilegeId(user.getPrivilegeId());
        userId.setUpdatedAt(new Date());

        userRepo.save(userId);

        return ResponseEntity.ok("Update user successfully.");
    }

    @DeleteMapping(value = "delete/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        User userDeleted = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id not found" + id));

        userRepo.delete(userDeleted);

        return ResponseEntity.ok("Delete user id: " + id + " successfully.");
    }
}
