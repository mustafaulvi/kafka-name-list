# User API Service

This service acts as a bridge between the frontend and Kafka messaging system. It provides REST APIs for the frontend and communicates with the User Storage Service through Kafka.

## Features

- REST API for user operations
- Kafka message producer for user creation
- Asynchronous user list retrieval through Kafka

## API Endpoints

### Create User
```http
POST /api/users
Content-Type: application/json

{
    "firstName": "John",
    "lastName": "Doe"
}
```

### Get Users
```http
GET /api/users
```

## Kafka Topics

- Produces to: `user-topic` (new user creation)
- Produces to: `user-list-request-topic` (request for user list)
- Consumes from: `user-list-response-topic` (user list response)

## Technical Stack

- Spring Boot 3.1.5
- Spring for Apache Kafka
- Spring Web
- Lombok

## Local Development

1. Requirements:
   - Java 17
   - Maven
   - Kafka

2. Configuration:
   Application configuration is in `src/main/resources/application.yml`

3. Building:
   ```bash
   mvn clean package
   ```

4. Running:
   ```bash
   java -jar target/user-api-service-1.0.0.jar
   ```

## Docker

Build the image:
```bash
docker build -t user-api-service .
```

Run the container:
```bash
docker run -p 8081:8081 user-api-service
``` 