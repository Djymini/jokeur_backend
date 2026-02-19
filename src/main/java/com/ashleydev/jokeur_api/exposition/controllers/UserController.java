package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal UserEntity user) {
        Map<String, Object> userInfo = new HashMap<>();
        // userInfo.put("id", user.getId());
        userInfo.put("id", user.getUsername());
        // userInfo.put("email", user.getEmail());
        // userInfo.put("email", user.ge);
        userInfo.put("role", user.getRole().name());
        return ResponseEntity.ok(userInfo);
    }
}
