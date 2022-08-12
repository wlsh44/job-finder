FROM openjdk:17

ARG JAR_FILE_PATH=build/libs/*.jar

COPY ${JAR_FILE_PATH} app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]