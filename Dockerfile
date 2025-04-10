# Use the OpenJDK 23 image as the base image
FROM openjdk:23-jdk

# Set the working directory inside the container to /app
WORKDIR /app

# Copy the jar file into the container's working directory
# This assumes that the .jar file is in the root of your project
COPY target/*.jar /app/app.jar

# Expose port 8080 (assuming your app runs on this port)
EXPOSE 8080

# Specify the entry point to run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
