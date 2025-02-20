# Vue User App

Frontend application for the Kafka User Management System.

## Features

- User registration form
- User list display
- Integration with User API Service

## Project Setup

```bash
# Install dependencies
npm install

# Serve for development
npm run serve

# Build for production
npm run build
```

## Docker

Build the image:
```bash
docker build -t vue-user-app .
```

Run the container:
```bash
docker run -p 8080:80 vue-user-app
```

## Configuration

The API URL can be configured in `src/services/userService.js` 