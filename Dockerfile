FROM gradle:6-jdk17 AS build
COPY . /opt/

WORKDIR /opt/

RUN ./gradlew connector:build

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /opt/connector/build/libs/connector.jar /opt/connector.jar

WORKDIR /opt/
ENTRYPOINT ["java","-jar","/opt/connector.jar"]