FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy wrapper and basic build files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (this layer is cached)
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copy actual source code
COPY src src

# Build the application skipping tests
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/product-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
