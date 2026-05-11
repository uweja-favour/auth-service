# ─────────────────────────────────────────────
# 1. BUILD STAGE
# ─────────────────────────────────────────────
FROM gradle:8.7-jdk21 AS build

WORKDIR /app

COPY . .

RUN ./gradlew :auth-service:bootJar --no-daemon


# ─────────────────────────────────────────────
# 2. RUNTIME STAGE
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/auth-service/build/libs/*.jar app.jar

EXPOSE 8080

# FORCE dev profile (equivalent to bootRun --args)
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT --spring.profiles.active=dev"]