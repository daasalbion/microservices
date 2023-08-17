# Start with a base image containing Java runtime
FROM eclipse-temurin

# Add Maintainer Info
LABEL maintainer="derlisarguello01@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 9003
EXPOSE 9003

# The application's jar file
ARG JAR_FILE=target/identity-service-0.0.2-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} identity-service.jar

# Run the jar file
ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-jar", "/identity-service.jar"]
