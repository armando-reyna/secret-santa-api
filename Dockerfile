FROM openjdk:8-jdk-alpine
VOLUME ["/tmp/storage", "/storage:/storage"]
COPY target/*.jar secretsanta.jar
ENTRYPOINT ["java", "-jar", "/secretsanta.jar"]
EXPOSE 8080
