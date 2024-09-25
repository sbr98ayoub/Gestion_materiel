# Step 1: Use JDK 21 as the base image
FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y libfreetype6 libfontconfig1
# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the packaged jar file into the container
# Assuming the jar is built using Maven and located in the target directory
COPY target/*.jar /app/myapp.jar


# Step 4: Copy resources (images, fonts, etc.) used by JasperReports into the container
COPY src/main/resources/reports /app/reports
# Step 5: Expose the port defined in application.properties (8084)
EXPOSE 8084

# Step 6: Environment variables for database configuration
ENV JDBC_DATABASE_URL=jdbc:mysql://host.docker.internal:3306/parc
ENV JDBC_DATABASE_USERNAME=root
ENV JDBC_DATABASE_PASSWORD=""

# Step 6: Run the application with the specified jar file
ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]


