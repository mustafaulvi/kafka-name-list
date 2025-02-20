# Build stage
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

# Maven'ı yükle
RUN apk add --no-cache maven

# Önce shared-models'i kopyala ve build et
COPY shared-models shared-models
RUN cd shared-models && mvn clean install

# Ana servisi build et
COPY user-storage-service/pom.xml pom.xml
COPY user-storage-service/src src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"] 