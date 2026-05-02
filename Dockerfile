# Step 1: Build stage (Maven + JDK)
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom and download dependencies first (cache optimization)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy full project
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests


# Step 2: Run stage (lightweight)
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]