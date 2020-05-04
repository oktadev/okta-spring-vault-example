# Tutorial: Secure Secrets With Spring Cloud Config and Vault

This repository contains all the code for testing a Spring Cloud Configuration Server using Vault as backend, and a demo client application with Okta OIDC authentication.

Please read [Secure Secrets With Spring Cloud Config and Vault](https://developer.okta.com/blog/2020/05/04/spring-vault) to see how this app was created.

**Prerequisites**: [Java 8](https://adoptopenjdk.net/)+ and [Docker](https://docs.docker.com/engine/install/).

> [Okta](https://developer.okta.com/) has Authentication and User Management APIs that reduce development time with instant-on, scalable user infrastructure. Okta's intuitive API and expert support make it easy for developers to authenticate, manage, and secure users and roles in any application.

* [Getting Started](#getting-started)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example, run the following commands:

```bash
git clone https://github.com/oktadeveloper/okta-spring-vault-example.git
```

## Create the OIDC Application in Okta

For the Okta authentication set up, register for a [free developer account](https://developer.okta.com/signup/). After you log in, go to **API** > **Authorization Servers** and copy your Issuer URI into a text editor.

Then go to **Applications** and create a new **Web** application. Configure it as follows:

- Name: `Vault Demo`
- Base URIs: `http://localhost:8080/`
- Login redirect URIs: `http://localhost:8080/login/oauth2/code/okta`
- Logout redirect URIs: `http://localhost:8080`
- Grant type allowed: 
  - [x] Authorization Code
  - [x] Refresh Token

Click **Done** and copy the **Client ID** and **Client secret** into a text editor for later. Go to **API** > **Authorization Servers** and copy the **default** issuer URI.


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
vault kv put secret/vault-demo-app,dev \ spring.security.oauth2.client.registration.oidc.client-id="{yourClientID}" \ spring.security.oauth2.client.registration.oidc.client-secret="{yourClientSecret}" \ spring.security.oauth2.client.provider.oidc.issuer-uri="{yourIssuerURI}"
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

Go to http://localhost:8080 and login with Okta.

## Links

This example uses the following libraries:

* [HashiCorp Vault](https://www.vaultproject.io/)
* [Okta Spring Boot Starter](https://github.com/oktadeveloper/generator-jhipster-ionic)
* [Spring Cloud Config](https://spring.io/projects/spring-cloud-config)

## Help

Please post any questions as comments on the [blog post](https://developer.okta.com/blog/2020/05/04/spring-vault), or visit our [Okta Developer Forums](https://devforum.okta.com/). You can also post a question to Stack Overflow with the ["okta" tag](https://stackoverflow.com/questions/tagged/okta).

## License

Apache 2.0, see [LICENSE](LICENSE).
