FROM gradle:6-jdk17 AS build
COPY . /opt/

WORKDIR /opt/

RUN --mount=type=secret,id=GIT_AUTH_TOKEN env TOKEN=$(cat /run/secrets/GIT_AUTH_TOKEN) USERNAME=someuser sh -c './gradlew connector:build'

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /opt/connector/build/libs/merlot-connector.jar /opt/connector.jar

WORKDIR /opt/
RUN touch vault.properties

ENTRYPOINT ["java","-jar","/opt/connector.jar"]