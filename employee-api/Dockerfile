# Use OpenJDK 17 based on Alpine Linux
FROM openjdk:17-alpine

# Set the working directory inside the container
WORKDIR /opt

# Copy the Gradle build artifact into the container
COPY build/libs/*.jar /opt/app.jar

# Define the command to run the application
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
