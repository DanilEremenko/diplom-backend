FROM gradle:8.8-jdk21 AS builder

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем только нужное — точно по путям
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src
RUN ls -l
RUN sed -i 's/\r$//' gradlew
RUN chmod +x gradlew
RUN ./gradlew --version
RUN ./gradlew --no-daemon clean bootJar -x test


# Финальный образ
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
