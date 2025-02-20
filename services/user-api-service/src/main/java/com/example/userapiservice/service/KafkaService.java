package com.example.userapiservice.service;

import com.example.shared.model.User;
import com.example.shared.model.UserListRequest;
import com.example.shared.model.UserListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableKafka
public class KafkaService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Map<String, CompletableFuture<UserListResponse>> pendingRequests = new ConcurrentHashMap<>();

    public void sendUser(User user) {
        try {
            log.info("Sending user to Kafka: {}", user);
            kafkaTemplate.send("user-topic", user)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Successfully sent user with offset: {}", result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send user", ex);
                    }
                });
        } catch (Exception e) {
            log.error("Error sending user", e);
        }
    }

    public CompletableFuture<UserListResponse> getUserList() {
        String requestId = UUID.randomUUID().toString();
        UserListRequest request = new UserListRequest(requestId);
        
        CompletableFuture<UserListResponse> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);
        
        log.info("Sending user list request with id: {}", requestId);
        kafkaTemplate.send("user-list-request-topic", request)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    pendingRequests.remove(requestId);
                    future.completeExceptionally(ex);
                    log.error("Failed to send user list request", ex);
                } else {
                    log.info("Successfully sent user list request with offset: {}", result.getRecordMetadata().offset());
                }
            });
        
        return future.orTimeout(30, TimeUnit.SECONDS)  // Timeout süresini 30 saniyeye çıkaralım
            .whenComplete((response, error) -> {
                pendingRequests.remove(requestId);
                if (error != null) {
                    log.error("Error getting user list for request: " + requestId, error);
                } else {
                    log.info("Received response for request {}: {}", requestId, response);
                }
            });
    }

    @KafkaListener(topics = "user-list-response-topic", groupId = "user-api-group")
    public void handleUserListResponse(UserListResponse response) {
        log.info("Received user list response for request: {}", response.getRequestId());
        CompletableFuture<UserListResponse> future = pendingRequests.get(response.getRequestId());
        if (future != null) {
            future.complete(response);
            log.info("Completed future for request: {}", response.getRequestId());
        } else {
            log.warn("No pending request found for id: {}", response.getRequestId());
        }
    }
} 