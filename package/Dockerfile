# Stage 1: Build the application using Gradle official image
FROM gradle:8.8-jdk17-jammy AS build-env

WORKDIR /app

# Copy Gradle wrapper and build files for better caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Grant execute permission for gradlew
RUN chmod +x gradlew

# Download dependencies (caching step)
RUN ./gradlew build -x test --stacktrace || return 0

# Copy the rest of the project files
COPY . .

# Build the application using Gradle Wrapper, skipping tests
RUN ./gradlew clean build --stacktrace

# Stage 2: Package the application using a lightweight image
FROM openjdk:17-ea-3-buster

WORKDIR /app

# Install locales package and set UTF-8 as default locale
RUN apt-get update && apt-get install -y locales && \
    sed -i '/en_US.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen en_US.UTF-8

# Set environment variables for locale
ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US:en \
    LC_ALL=en_US.UTF-8

# Create a non-root user to run the application
RUN useradd -m -u 1000 appuser

# Copy the built JAR file from the build stage
COPY --from=build-env /app/build/libs/*.jar /app/app.jar

# Set ownership of the JAR file
RUN chown appuser:appuser /app/app.jar

# Switch to the non-root user
USER appuser

# Expose port 8080
EXPOSE 8080

# Run the JAR file with UTF-8 encoding
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/app/app.jar"]