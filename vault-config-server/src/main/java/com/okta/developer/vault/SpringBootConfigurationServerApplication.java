package com.okta.developer.vault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class SpringBootConfigurationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootConfigurationServerApplication.class, args);
	}

}
