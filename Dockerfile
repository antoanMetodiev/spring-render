# Етап 1: Създаване на JAR файла
FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    wget \
    unzip \
    curl \
    git \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Копиране на всички файлове от проекта в контейнера
COPY . .

# Дава права за изпълнение на gradlew (ако е нужно)
RUN chmod +x ./gradlew

# Изграждане на JAR файла
RUN ./gradlew bootJar --no-daemon

# Етап 2: Създаване на финален образ
FROM openjdk:17-jdk-slim

WORKDIR /app

# Копиране на JAR файла от предишния етап
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

# Стартиране на приложението
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
