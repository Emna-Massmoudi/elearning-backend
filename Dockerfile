FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/elearning-api-0.0.3-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.address=0.0.0.0", "-jar", "app.jar"]