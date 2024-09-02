# Use a base image with Java
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/*.jar app.jar

# Set environment variables
ENV REDIS_HOST=my-redis

# Expose port 80
EXPOSE 80

# Define the command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]