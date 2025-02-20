package com.example.userstorageservice.kafka;

import com.example.userstorageservice.model.User;
import com.example.userstorageservice.model.UserListRequest;
import com.example.userstorageservice.model.UserListResponse;
import com.example.userstorageservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConsumer {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "user-topic", groupId = "user-storage-group")
    public void consumeUser(User user) {
        log.info("Received user: {}", user);
        userRepository.save(user);
    }

    @KafkaListener(topics = "user-list-request-topic", groupId = "user-storage-group")
    public void handleUserListRequest(UserListRequest request) {
        log.info("Received user list request: {}", request);
        UserListResponse response = new UserListResponse(
            userRepository.findAll(),
            request.getRequestId()
        );
        kafkaTemplate.send("user-list-response-topic", response);
        log.info("Sent user list response: {}", response);
    }
} 