FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline generate-sources -B || true
RUN find /root/.m2 -name "*.exe" -exec chmod +x {} \; 2>/dev/null || true
RUN find /app/target -name "*.exe" -exec chmod +x {} \; 2>/dev/null || true

COPY src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]