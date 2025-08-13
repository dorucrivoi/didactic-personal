package com.example.dp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan(basePackages = {
		"com.example.dp.model.professor.entity",
		"com.example.dp.model.schoolclass.entity"
})
@SpringBootApplication
public class DidacticPersonalApplication {

	public static void main(String[] args) {
		SpringApplication.run(DidacticPersonalApplication.class, args);
	}

}
