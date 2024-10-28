FROM openjdk:17-jdk-alpine

ARG JAR_FILE=build/libs/bokji-mate-0.0.1-SNAPSHOT.jar

WORKDIR /app

COPY ${JAR_FILE} myapp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "myapp.jar"]
