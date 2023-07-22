FROM openjdk:19-jdk-slim

WORKDIR /app

COPY . /app

RUN ./gradlew clean build -x test

COPY ./build/libs/*.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
