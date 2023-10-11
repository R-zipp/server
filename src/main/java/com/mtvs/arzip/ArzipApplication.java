package com.mtvs.arzip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ArzipApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArzipApplication.class, args);
	}

}
