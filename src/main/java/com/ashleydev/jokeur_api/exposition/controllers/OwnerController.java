package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal OwnerEntity owner) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", owner.getId());
        userInfo.put("email", owner.getEmail());
        userInfo.put("role", owner.getRole().name());
        return ResponseEntity.ok(userInfo);
    }
}
