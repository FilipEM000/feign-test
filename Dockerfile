FROM eclipse-temurin:21-jdk-alpine
COPY target/test-0.0.1-SNAPSHOT.jar test-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/test-0.0.1-SNAPSHOT.jar"]