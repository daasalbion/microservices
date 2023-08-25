# Start with a base image containing Java runtime
FROM eclipse-temurin

# Add Maintainer Info
LABEL maintainer="derlisarguello01@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 9004
EXPOSE 9004

# The application's jar file
ARG JAR_FILE=target/user-service-0.0.2-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} user-service.jar

# Run the jar file
ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-jar","/user-service.jar"]
