# Use the OpenJDK 17 JDK image based on Alpine Linux
FROM openjdk:17-jdk-alpine
# Define an argument for the JAR file location
ARG JAR_FILE=target/*.jar
# Copy the JAR file into the container at /app/app.jar
COPY ${JAR_FILE} app.jar
# Specify the command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]

