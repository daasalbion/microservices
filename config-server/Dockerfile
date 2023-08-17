# Start with a base image containing Java runtime
FROM eclipse-temurin

# Add Maintainer Info
LABEL maintainer="derlisarguello01@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8888
EXPOSE 8888

# The application's jar file
ARG JAR_FILE=target/config-server-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} config-server.jar

# Run the jar file
ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-Djava.security.egd=file:/dev/./urandom","-jar", "/config-server.jar"]
