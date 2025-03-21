FROM gradle:8.12.1-jdk21 AS builder

WORKDIR /builder

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
RUN gradle --no-daemon build || return 0

COPY . .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /builder/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
