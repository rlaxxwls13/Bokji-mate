# 1. 베이스 이미지 설정 (Java 17 사용)
FROM openjdk:17-jdk-slim

# 2. 애플리케이션 JAR 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 3. 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]
