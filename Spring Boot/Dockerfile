# syntax=docker/dockerfile:experimental
FROM maven:3.8.3-openjdk-17 AS builder
# Configuration de l'environnement de construction
WORKDIR /Courzelo
COPY Courzelo/pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline
# Copie seulement du code source nécessaire et compilation de l'application
COPY Courzelo/src/main/java src/main/java/
COPY Courzelo/src/main/resources src/main/resources/
RUN --mount=type=cache,target=/root/.m2 mvn package

# Utilisation d'une image Alpine pour réduire la taille
FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
COPY --from=builder /Courzelo/target/Courzelo-0.0.1-SNAPSHOT.jar Courzelo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "/Courzelo-0.0.1-SNAPSHOT.jar"]