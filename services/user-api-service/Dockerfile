FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

# Maven wrapper dosyalarını kopyala
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Bağımlılıkları indir
RUN ./mvnw dependency:go-offline

# Kaynak kodları kopyala ve derle
COPY src src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"] 