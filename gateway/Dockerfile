# Start with a base image containing Java runtime
FROM eclipse-temurin

# Add Maintainer Info
LABEL maintainer="derlisarguello01@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 9002
EXPOSE 9002

# The application's jar file
ARG JAR_FILE=target/gateway-0.0.2-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} gateway.jar

# Run the jar file
ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-Djava.security.egd=file:/dev/./urandom","-jar","/gateway.jar"]
