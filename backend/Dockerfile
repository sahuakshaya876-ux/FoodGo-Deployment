# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

RUN addgroup -S foodgo && adduser -S foodgo -G foodgo

COPY --from=build /build/target/foodgo-backend.jar app.jar

RUN chown foodgo:foodgo app.jar

USER foodgo

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080

ENV JAVA_OPTS=""
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
