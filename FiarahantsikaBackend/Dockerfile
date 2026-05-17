FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests -B || \
    (find /app/target/protoc-plugins -name "*.exe" -exec chmod +x {} \; && \
     mvn package -DskipTests -B)

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]