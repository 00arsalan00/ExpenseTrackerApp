# Use lightweight Java image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy jar into container
COPY target/*.jar app.jar

# Expose application port
EXPOSE 3090

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]