# Use the OpenJDK 23 image as the base image
FROM openjdk:23-jdk

# Set the working directory inside the container to /app
WORKDIR /app

# Copy the jar file from the root of your project directory to /app/app.jar
# This assumes your .jar file is located in the root of your project
COPY *.jar /app/app.jar

# Expose port 8080 (assuming your app runs on this port)
EXPOSE 8080

# Specify the entry point to run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
