# MERLOT conformant EDC Connector

This repository contains an EDC configuration which meets to the requirements of the MERLOT federation.

A list of all used EDC extensions can be found in the [gradle build file](./connector/build.gradle.kts).

## Build

To build this connector you need to provide a GitHub read-only token in order to be able to fetch maven packages from GitHub. You can create this token at https://github.com/settings/tokens with at least the scope "read:packages".

Once the token is generated you can use the command below by replacing the respective values:

    USERNAME=REPLACEME_GITHUB_USER TOKEN=REPLACEME_GITHUB_TOKEN ./gradlew connector:build

## Run

You can run the built EDC connector using:

    java -Dedc.fs.config=resources/provider/provider-configuration.properties -jar connector/build/libs/merlot-connector.jar

This will launch the EDC with an exemplary configuration located [here](./resources/provider/provider-configuration.properties). If you want to be able to transfer to/from IONOS S3 buckets you will also need to provide the respective edc.ionos.* parameters there.

## Deploy (Docker)

Similarly to the standalone build and run from above, you need to provide a GitHub  token in the [git_auth_token.txt](./secrets/git_auth_token.txt) file.
If you want to be able to transfer to/from IONOS S3 buckets you can configure the respective parameters in the [edc_ionos_secrets.txt](./secrets/edc_ionos_secrets.txt) file.

To deploy the EDC together with a properly configured reverse proxy, you can then use the following command:

    docker compose up

This will build and launch the EDC as well as forward all EDC-API requests through the nginx-proxy at http://localhost/myedc (which is the base URL of your new connector). 
All EDC API endpoints can then then be accessed using these URLs:

    http://localhost/myedc/api
    http://localhost/myedc/management
    http://localhost/myedc/protocol
    http://localhost/myedc/public
    http://localhost/myedc/control

This structure (and hence the provided nginx configuration) follows the requirements of the MERLOT marketplace.

## Deploy (Helm)
TODO