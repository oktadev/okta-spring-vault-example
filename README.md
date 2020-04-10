# Tutorial: Secure Secrets With Spring Cloud Config and Vault

This repository contains all the code for testing a Spring Cloud Configuration Server using Vault as backend, and a demo client application with Okta OIDC authentication.

**Prerequisites:**

- Java 8

## Getting Started

To install this example, run the following commands:
```bash
git clone https://github.com/indiepopart/spring-vault.git
```

## Create the OIDC Application in Okta

Log in to your Okta Developer account (or [sign up](https://developer.okta.com/signup/) if you don’t have an account).
Setup the client application:

From the **Applications** page, choose **Add Application**. On the Create New Application page, select **Web**. Set the following values:
- Name: vault-demo-app
- Base URIs: http://localhost:8080/
- Login redirect URIs: http://localhost:8080/login/oauth2/code/oidc
- Logout redirect URIs: http://localhost:8080
- Grant type allowed: Authorization Code, Refresh Token

Copy the **ClientId** and **ClientSecret**. Go to the Dashboard home and copy the **Org URL** from the top right corner.


## Run Vault

```shell
docker pull vault
docker run --cap-add=IPC_LOCK \
-e 'VAULT_DEV_ROOT_TOKEN_ID=00000000-0000-0000-0000-000000000000' \
-p 8200:8200 \
-v {hostPath}:/vault/logs \
--name my-vault vault
```

Store the secrets:

```shell
docker exec -it my-vault /bin/sh
export VAULT_TOKEN="00000000-0000-0000-0000-000000000000"
export VAULT_ADDR="http://127.0.0.1:8200"
vault kv put secret/vault-demo-app,dev \ spring.security.oauth2.client.registration.oidc.client-id="{yourClientID}" \ spring.security.oauth2.client.registration.oidc.client-secret="{yourClientSecret}" \ spring.security.oauth2.client.provider.oidc.issuer-uri="{yourOrgUrl}"
```


## Run the applications with Maven

Run `vault-config-server`:

```shell
cd spring-vault/vault-config-server
./mvnw spring-boot:run
```

Run `vault-demo-app`:
```shell
SPRING_CLOUD_CONFIG_TOKEN=00000000-0000-0000-0000-000000000000 \
./mvnw spring-boot:run
```

Got to http://localhost:8080 and login with Okta.
