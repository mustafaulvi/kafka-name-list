package com.example.userapiservice.service;

import com.example.userapiservice.model.User;
import com.example.userapiservice.model.UserListRequest;
import com.example.userapiservice.model.UserListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ErrorHandlingDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableKafka
public class KafkaService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Map<String, CompletableFuture<UserListResponse>> pendingRequests = new ConcurrentHashMap<>();

    public void sendUser(User user) {
        log.info("Sending user: {}", user);
        kafkaTemplate.send("user-topic", user);
    }

    public CompletableFuture<UserListResponse> getUserList() {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<UserListResponse> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        log.info("Sending user list request with id: {}", requestId);
        UserListRequest request = new UserListRequest(requestId);
        kafkaTemplate.send("user-list-request-topic", request);

        // 30 saniye timeout ekle
        return future.orTimeout(30, TimeUnit.SECONDS)
            .whenComplete((response, throwable) -> {
                pendingRequests.remove(requestId);
                if (throwable != null) {
                    log.error("Error getting user list", throwable);
                }
            });
    }

    @KafkaListener(topics = "user-list-response-topic", groupId = "user-api-group")
    public void handleUserListResponse(UserListResponse response) {
        log.info("Received user list response: {}", response);
        CompletableFuture<UserListResponse> future = pendingRequests.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        }
    }

    public ConcurrentKafkaListenerContainerFactory<String, UserListResponse> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserListResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    private ConsumerFactory<String, UserListResponse> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }
} 