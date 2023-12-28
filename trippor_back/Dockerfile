FROM openjdk:8-jre
ARG JAR_FILE=target/trippor_backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar","app.jar"]