FROM openjdk:19-jdk-slim

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

RUN mv build/libs/*.jar /app

ENTRYPOINT ["java","-jar","/app/todo-0.0.1-SNAPSHOT.jar"]

