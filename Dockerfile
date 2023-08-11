# Use a specific OpenJDK version as base image
FROM openjdk:11

# Set environment variables for Spring Boot application
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql-container:3306/book?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=
ENV SPRING_DATASOURCE_DRIVER-CLASS-NAME=com.mysql.cj.jdbc.Driver
ENV SPRING_JPA_HIBERNATE_DDL-AUTO=update
ENV SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL57Dialect
ENV SERVER_PORT=8080

# Copy the Spring Boot JAR into the container
COPY target/*.jar /app.jar

# Expose the desired port for the Spring Boot application
EXPOSE ${SERVER_PORT}

# Run the Spring Boot application
CMD ["java", "-jar", "/app.jar"]
