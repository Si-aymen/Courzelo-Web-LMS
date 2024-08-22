package org.example.courzelo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class CourzeloApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourzeloApplication.class, args);
	}

}
