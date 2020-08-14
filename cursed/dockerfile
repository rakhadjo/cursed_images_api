FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.28-slim
VOLUME /tmp
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]
