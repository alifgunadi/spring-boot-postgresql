package com.backend.application.api.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.application.api.models.User;
import com.backend.application.api.services.JwtService;
import com.backend.application.api.services.UserService;

@RestController
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userDetailsService;

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User loginUser) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginUser.getUsername());

        final String token = jwtService.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();

        response.put("status", "OK");
        response.put("token", token);
        response.put("user", userDetails.getUsername());

        return ResponseEntity.ok(response);
    }
}
