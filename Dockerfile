# Use a specific OpenJDK version as base image
FROM openjdk:11

# Set environment variables for Spring Boot application
ENV SPRING_DATASOURCE_URL=jdbc:sqlserver://172.18.0.5:1433;databaseName=bookstore
ENV SPRING_DATASOURCE_USERNAME=sa
ENV SPRING_DATASOURCE_PASSWORD=Esprit2023
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.microsoft.sqlserver.jdbc.SQLServerDriver
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.SQLServer2012Dialect
ENV SERVER_PORT=1001

# Copy the Spring Boot JAR into the container
COPY target/*.jar /app.jar

# Expose the desired port for the Spring Boot application
EXPOSE ${SERVER_PORT}

# Run the Spring Boot application
CMD ["java", "-jar", "/app.jar"]
