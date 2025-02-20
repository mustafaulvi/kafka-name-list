package com.example.userstorageservice.kafka;

import com.example.shared.model.User;
import com.example.shared.model.UserListRequest;
import com.example.shared.model.UserListResponse;
import com.example.userstorageservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConsumer {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "user-topic", groupId = "user-storage-group")
    public void consumeUser(User user) {
        try {
            log.info("Received user to save: {}", user);
            User savedUser = userRepository.save(user);
            log.info("Successfully saved user: {}", savedUser);
        } catch (Exception e) {
            log.error("Error saving user", e);
        }
    }

    @KafkaListener(topics = "user-list-request-topic", groupId = "user-storage-group")
    public void handleUserListRequest(UserListRequest request) {
        try {
            log.info("Received user list request with id: {}", request.getRequestId());
            
            List<User> users = userRepository.findAll();
            log.info("Found {} users in database", users.size());
            
            UserListResponse response = new UserListResponse(users, request.getRequestId());
            log.info("Created response for request {}: {}", request.getRequestId(), response);
            
            // Yanıtı senkron olarak gönderelim
            var result = kafkaTemplate.send("user-list-response-topic", response).get(5, TimeUnit.SECONDS);
            log.info("Successfully sent response for request {} with offset: {}", 
                request.getRequestId(), result.getRecordMetadata().offset());
            
        } catch (Exception e) {
            log.error("Error handling user list request: " + request.getRequestId(), e);
        }
    }
} 