# Use a JDK 17 image
FROM openjdk:17-slim

# Install basic dependencies (curl, etc. — needed for mvnw sometimes)
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Give permission to the Maven wrapper script (just in case)
RUN chmod +x mvnw

# Expose port 8080
EXPOSE 8080

# Start the Quarkus development server
CMD ["./mvnw", "quarkus:dev"]
