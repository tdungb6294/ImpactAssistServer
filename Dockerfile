FROM openjdk:23-jdk
COPY ./build/libs/*.jar /app.jar
RUN chmod +x /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
