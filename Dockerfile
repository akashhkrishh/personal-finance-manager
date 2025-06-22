# Stage 1: Build with JDK 21
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml for caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Fix permission issue for mvnw
RUN chmod +x mvnw

# Download dependencies offline (cache)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the JAR, skip tests for faster build (optional)
RUN ./mvnw clean package -DskipTests

# Stage 2: Run with JRE 21
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/target/finance-manager-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
