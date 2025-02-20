# Kafka Learning Project

This project is developed as a microservices architecture example using Apache Kafka.

## Project Components

1. Frontend (Vue.js)
   - User information input form
   - Registered users display table

2. User API Service (Spring Boot)
   - Receive user information
   - Send messages via Kafka
   - Query user list

3. User Storage Service (Spring Boot)
   - Process messages received through Kafka
   - Database operations
   - User information query API

## Technologies

- Frontend: Vue.js 3
- Backend: Spring Boot 3
- Messaging: Apache Kafka
- Database: PostgreSQL
- Containerization: Docker

## Setup

1. Requirements:
   - Docker and Docker Compose
   - Java 17
   - Node.js 16+

2. Running the project:
   ```bash
   docker-compose -f docker/docker-compose.yml up -d
   ```

## Project Structure

- `frontend/`: Vue.js user interface
- `services/`: Spring Boot microservices
- `docker/`: Docker configuration files

## Architecture Overview

The project consists of three main components that communicate with each other:

1. A Vue.js frontend that allows users to input and view data
2. A Spring Boot API service that processes requests and produces Kafka messages
3. A Spring Boot storage service that consumes Kafka messages and handles database operations

## Development

Each component can be developed and tested independently. Refer to the README files in individual component directories for specific development instructions.

## Deployment

The entire application stack can be deployed using Docker Compose. Each component is containerized and configured to communicate with other services. 