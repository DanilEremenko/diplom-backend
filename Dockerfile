FROM openjdk:17-jdk-slim

WORKDIR /app

COPY gradle gradle
COPY gradlew build.gradle ./
COPY src ./src

RUN ./gradlew clean build -x test

CMD ["./gradlew", "bootRun"]