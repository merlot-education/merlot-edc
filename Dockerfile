#  Copyright 2024 Dataport. All rights reserved. Developed as part of the MERLOT project.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

FROM gradle:6-jdk17 AS build
COPY . /opt/

WORKDIR /opt/

RUN --mount=type=secret,id=GIT_AUTH_TOKEN env TOKEN=$(cat /run/secrets/GIT_AUTH_TOKEN) USERNAME=someuser sh -c './gradlew connector:build'

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /opt/connector/build/libs/merlot-connector.jar /opt/connector.jar

WORKDIR /opt/
RUN touch vault.properties

ENTRYPOINT ["java","-jar","/opt/connector.jar"]