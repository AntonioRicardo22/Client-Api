FROM openjdk:21-jdk-slim

WORKDIR /app

COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw clean package -DskipTests

RUN addgroup --system spring && adduser --system spring --ingroup spring

RUN chown -R spring:spring /app
USER spring:spring

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

CMD ["java", "-jar", "target/cliente-api-0.0.1-SNAPSHOT.jar"]
