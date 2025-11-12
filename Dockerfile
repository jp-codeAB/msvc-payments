FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8087

ENV SPRING_PROFILES_ACTIVE=dev \
    CONFIG_SERVER_URI=http://config-server:8888 \
    EUREKA_URI=http://eureka-server:8761/eureka/ \
    RABBITMQ_HOST=rabbitmq \
    RABBITMQ_PORT=5672 \
    RABBITMQ_USER=guest \
    RABBITMQ_PASS=guest \
    DB_HOST=postgres \
    DB_PORT=5432 \
    DB_NAME=paydb \
    DB_USER=postgres \
    DB_PASS=100juanU

HEALTHCHECK CMD curl -f http://localhost:8087/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]

