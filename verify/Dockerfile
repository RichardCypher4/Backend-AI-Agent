# Step 1: Use an official OpenJDK 21 runtime as base image
FROM azul/zulu-openjdk

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the Maven-built JAR file from your local target folder to the container
COPY target/verify-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the port your Spring Boot app runs on
EXPOSE 8080

# Step 5: Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
