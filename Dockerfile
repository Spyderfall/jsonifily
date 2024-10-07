# Stage 1: Build the application
FROM maven:3.8.6-openjdk-11-slim AS builds

# Set the working directory inside the container
WORKDIR /app/jsonifily/

# Copy the Maven project files
COPY pom.xml /app/jsonifily/
COPY src /app/jsonifily/

# Build the application
RUN mvn clean package

# Stage 2
# Use an official Java runtime as a parent image
FROM openjdk:11-jdk-slim

WORKDIR /app/jsonifily/

# Copy the built jar to a new image layer
COPY --from=build /app/target/*.jar jsonifily.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "jsonifily.jar"]
