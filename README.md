# MERLOT EDC Connector

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

Furthermore you need to specify the parameters required for DAPS authentication in order to talk to other EDCs in the MERLOT Federation.

## Deploy (Docker)

Similarly to the standalone build and run from above, you need to provide a GitHub  token in the [git_auth_token.txt](./secrets/git_auth_token.txt) file.
If you want to be able to transfer to/from IONOS S3 buckets you can configure the respective parameters in the [edc_ionos_secrets.txt](./secrets/edc_ionos_secrets.txt) file. Finally you will also need the parameters and a keystore file containing the DAPS certificate to authenticate against the DAPS server.

To deploy the EDC together with a properly configured reverse proxy, you can then use the following command:

    docker compose up

This will build and launch the EDC as well as forward all EDC-API requests through the nginx-proxy at http://localhost/myedc (which is the base URL of your new connector). 
All EDC API endpoints can then be accessed using these URLs:

    http://localhost/myedc/api
    http://localhost/myedc/management
    http://localhost/myedc/protocol
    http://localhost/myedc/public
    http://localhost/myedc/control

This structure (and hence the provided nginx configuration) follows the requirements of the MERLOT marketplace.

## Deploy (Helm)
### Prerequisites
Before you begin, ensure you have Helm installed and configured to the desired Kubernetes cluster.

### Setting Up Minikube (if needed)
If you don't have a Kubernetes cluster set up, you can use Minikube for local development. Follow these steps to set up Minikube:

1. **Install Minikube:**
   Follow the instructions [here](https://minikube.sigs.k8s.io/docs/start/) to install Minikube on your machine.

2. **Start Minikube:**
   Start Minikube using the following command:
   ```
   minikube start
   ```
3. **Verify Minikube Status:**
   Check the status of Minikube to ensure it's running:   
   ```
   minikube status
   ```

### Usage
1. **Clone the Repository:**
   Clone the repository containing the Helm chart:
   ```
   git clone https://github.com/merlot-education/gitops.git
   ```

2. **Navigate to the Helm Chart:**
   Change into the directory of the Helm chart:
   ```
   cd gitops/charts/merlot-edc
   ```

3. **Customize Values (if needed):**
   If you need to customize any values, modify the values.yaml file in this directory according to your requirements. This file contains configurable parameters such as image repository, tag, service ports, etc. An example containing the values used in Merlot dev environment is available in gitops/environments/dev/edc-1.yaml

4. **Install the Chart:**
   Run the following command to install the chart from the local repository:
   ```
   helm install [RELEASE_NAME] .
   ```
   Replace [RELEASE_NAME] with the name you want to give to this deployment. In this case it can be edc-1.

5. **Verify Deployment:**
   Check the status of your deployment using the following commands:
   ```
   kubectl get pods
   kubectl get services
   ```

### Additional Resources 
- [Helm Documentation](https://helm.sh/docs/)
