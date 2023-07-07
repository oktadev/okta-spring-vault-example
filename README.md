# Tutorial: Secure Secrets With Spring Cloud Config and Vault

This repository contains all the code for testing a Spring Cloud Configuration Server using Vault as backend, and a demo client application with Okta OIDC authentication.

Please read [Secure Secrets With Spring Cloud Config and Vault](https://developer.okta.com/blog/2022/10/20/spring-vault) to see how this app was created.

**Prerequisites:**

- [Java OpenJDK 17](https://jdk.java.net/java-se-ri/17)
- [Okta CLI 0.10.0](https://cli.okta.com)
- [Docker 20.10.12](https://docs.docker.com/engine/install/)
- [HTTPie 3.2.1](https://httpie.io/docs/cli/installation)
- [Vault 1.14](https://hub.docker.com/r/hashicorp/vault)

> [Okta](https://developer.okta.com/) has Authentication and User Management APIs that reduce development time with instant-on, scalable user infrastructure. Okta's intuitive API and expert support make it easy for developers to authenticate, manage, and secure users and roles in any application.

* [Getting Started](#getting-started)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example, run the following commands:

```bash
git clone https://github.com/oktadev/okta-spring-vault-example.git
```

## Create an OIDC Application in Okta

Open a command line session and navigate into the `okta-spring-vault-example/vault-demo-app` directory.

To get a free Okta developer account, install the [Okta CLI](https://cli.okta.com/) and run `okta register` to sign up for a new account. If you already have an account, run `okta login`. Then, run `okta apps create`. Select the default app name, or change it as you see fit. Choose **Web** and press **Enter**.

Select **Okta Spring Boot Starter**. Accept the default Redirect URI values provided for you. That is, a Login Redirect of `http://localhost:8080/login/oauth2/code/okta` and a Logout Redirect of `http://localhost:8080`.

<p>
<details>
  <summary>What does the Okta CLI do?</summary>

  The Okta CLI will create an OIDC Web App in your Okta Org. It will add the redirect URIs you specified and grant access to the Everyone group. You will see output like the following when it’s finished:

  ```shell
  Okta application configuration has been written to: /path/to/app/src/main/resources/application.properties
  ```

  Open `src/main/resources/application.properties` to see the issuer and credentials for your app.

  ```shell
  okta.oauth2.issuer=https://dev-133337.okta.com/oauth2/default
  okta.oauth2.client-id=0oab8eb55Kb9jdMIr5d6
  okta.oauth2.client-secret=NEVER-SHOW-SECRETS
  ```

  **NOTE**: You can also use the Okta Admin Console to create your app. See [Create a Spring Boot App](https://developer.okta.com/docs/guides/sign-into-web-app/springboot/create-okta-application/) for more information.

</details>
</p>

Copy the values from `src/main/resources/application.properties` and delete the file.

## Create an OIDC Application in Auth0

Sign up at [Auth0](https://auth0.com/signup) and install the [Auth0 CLI](https://github.com/auth0/auth0-cli). Then run:

```shell
auth0 login
```

The terminal will display a device confirmation code and open a browser session to activate the device. After you log in, the terminal will display a success message.

Then, create a client app:

```shell
auth0 apps create \
  --name "Spring Boot + Vault" \
  --description "Demo project of a Spring Boot application with Vault protected secrets" \
  --type regular \
  --callbacks http://localhost:8080/login/oauth2/code/okta \
  --logout-urls http://localhost:8080 \
  --reveal-secrets
```

## Run Vault

Pull the Vault image.

```shell
docker pull hashicorp/vault:1.14
```
Run a container, make sure to replace `{hostPath}` with a local directory path, such as `/tmp/vault`:

```shell
docker run --cap-add=IPC_LOCK \
-e 'VAULT_DEV_ROOT_TOKEN_ID=00000000-0000-0000-0000-000000000000' \
-p 8200:8200 \
-v {hostPath}:/vault/logs \
--name my-vault vault
```

Open an interactive terminal with Vault:

```shell
docker exec -it my-vault /bin/sh
```

In the terminal, store the secrets by executing the following code. Replace with the values returned by Okta CLI.

```shell
export VAULT_TOKEN="00000000-0000-0000-0000-000000000000"
export VAULT_ADDR="http://127.0.0.1:8200"
vault kv put secret/vault-demo-app,dev \
okta.oauth2.clientId="{yourClientId}" \
okta.oauth2.clientSecret="{yourClientSecret}" \
okta.oauth2.issuer="{yourIssuerURI}"
```

## Run the applications with Maven

Run `vault-config-server`:

```shell
cd okta-spring-vault-example/vault-config-server
./mvnw spring-boot:run
```

Run `vault-demo-app`:
```shell
SPRING_CLOUD_CONFIG_TOKEN=00000000-0000-0000-0000-000000000000 \
./mvnw spring-boot:run
```

Go to `http://localhost:8080` and log in with Okta.

## Links

This example uses the following libraries:

* [HashiCorp Vault](https://www.vaultproject.io/)
* [Okta Spring Boot Starter](https://github.com/okta/okta-spring-boot)
* [Spring Cloud Config](https://spring.io/projects/spring-cloud-config)

## Help

Please post any questions as comments on the [blog post](https://developer.okta.com/blog/2022/10/20/spring-vault), or visit our [Okta Developer Forums](https://devforum.okta.com/). You can also post a question to Stack Overflow with the ["okta" tag](https://stackoverflow.com/questions/tagged/okta).

## License

Apache 2.0, see [LICENSE](LICENSE).
