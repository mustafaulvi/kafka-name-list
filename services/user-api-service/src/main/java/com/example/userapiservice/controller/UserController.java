package com.example.userapiservice.controller;

import com.example.shared.model.User;
import com.example.userapiservice.model.UserListResponse;
import com.example.userapiservice.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final KafkaService kafkaService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        log.info("Creating user: {}", user);
        kafkaService.sendUser(user);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<User>>> getUsers() {
        log.info("Getting user list");
        return kafkaService.getUserList()
                .thenApply(response -> {
                    log.info("Got user list: {}", response);
                    return ResponseEntity.ok(response.getUsers());
                })
                .exceptionally(throwable -> {
                    log.error("Error getting user list", throwable);
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
                });
    }
} 