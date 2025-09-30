# --- STAGE 1: BUILD STAGE ---
# Use Eclipse Temurin (more stable than generic OpenJDK for Java 21+)
# Note: Java 25 may not be available yet, using Java 21 as fallback
FROM eclipse-temurin:21-jdk-alpine AS build

# Install Maven (Alpine doesn't include it by default)
RUN apk add --no-cache maven

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file first to leverage Docker's caching
# If pom.xml doesn't change, Docker won't re-run the dependency download
COPY code/pom.xml .

# Download dependencies separately to build cache layer
# Use -B for batch mode (non-interactive) and -q for quiet output
RUN mvn dependency:go-offline -B -q

# Copy the rest of the source code
COPY code/src /app/src

# Package the application into a JAR file
# Add more Maven optimization flags
RUN mvn package -DskipTests -B -q --no-transfer-progress

# --- STAGE 2: RUNTIME STAGE ---
# Use Eclipse Temurin JRE for better stability and security
FROM eclipse-temurin:21-jre-alpine

# Install wget for health checks and create a non-root user for security
RUN apk add --no-cache wget && \
    addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set the working directory
WORKDIR /app

# Change ownership of the working directory
RUN chown -R appuser:appgroup /app

# Copy the final JAR file from the 'build' stage
# Based on your pom.xml: realestate-analyser-0.0.1-SNAPSHOT.jar
COPY --from=build --chown=appuser:appgroup /app/target/realestate-analyser-*.jar app.jar

# Switch to non-root user
USER appuser

# Expose the port your Spring Boot application runs on (default is 8080)
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Define the command to run the application with optimized JVM settings
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+OptimizeStringConcat", \
    "-XX:+UseG1GC", \
    "-jar", "app.jar"]
