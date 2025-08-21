# Use a lightweight OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Download Gradle dependencies
RUN ./gradlew build --no-daemon --refresh-dependencies

# Copy source code
COPY src/ src/

# Default command to run the app
CMD ["./gradlew", "bootRun"]
