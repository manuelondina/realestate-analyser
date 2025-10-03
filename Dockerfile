# --- STAGE 1: BUILD STAGE ---
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copy everything Maven needs (pom.xml + modules + wrapper)
COPY code/pom.xml .
COPY code/.mvn ./.mvn
COPY code/mvnw .
COPY code/mvnw.cmd .

# Copy submodules BEFORE running dependency:go-offline
COPY code/boot ./boot
COPY code/api ./api
COPY code/application ./application
COPY code/domain ./domain
COPY code/infrastructure ./infrastructure

RUN chmod +x mvnw

# Download dependencies (with modules available now)
RUN ./mvnw dependency:go-offline -B -q

# Package JAR (skip tests for faster build)
RUN ./mvnw package -DskipTests -B -q --no-transfer-progress

# --- STAGE 2: RUNTIME STAGE ---
FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache wget && \
    addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app
RUN chown -R appuser:appgroup /app

# ✅ Copy the final JAR from the correct module (boot)
COPY --from=build --chown=appuser:appgroup /app/boot/target/realestate-analyser-*.jar app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+OptimizeStringConcat", \
    "-XX:+UseG1GC", \
    "-jar", "app.jar"]

