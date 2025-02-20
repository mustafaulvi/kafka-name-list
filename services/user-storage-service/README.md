# User Storage Service

This service is responsible for storing user information received through Kafka and providing REST APIs to query the stored data.

## Features

- Consumes user information from Kafka topic
- Stores user data in PostgreSQL database
- Provides REST API for querying user data

## Technical Stack

- Spring Boot 3.1.5
- Spring Data JPA
- Spring for Apache Kafka
- PostgreSQL
- Lombok

## Local Development

1. Requirements:
   - Java 17
   - Maven
   - PostgreSQL
   - Kafka

2. Configuration:
   Application configuration is in `src/main/resources/application.yml`

3. Building:
   ```bash
   mvn clean package
   ```

4. Running:
   ```bash
   java -jar target/user-storage-service-1.0.0.jar
   ```

## API Endpoints

- GET `/api/users` - List all users
- GET `/api/users/{id}` - Get user by ID

## Kafka Configuration

- Consumer Group: `user-storage-group`
- Topic: `user-topic` 