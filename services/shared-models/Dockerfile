FROM eclipse-temurin:17-jdk-alpine
WORKDIR /workspace/app

# Maven'ı yükle
RUN apk add --no-cache maven

# POM ve kaynak kodları kopyala
COPY pom.xml .
COPY src src

# Paketi derle ve yerel Maven repository'ye yükle
RUN mvn clean install 