package ua.squirrel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SquirrelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SquirrelApplication.class, args);
	}

}
