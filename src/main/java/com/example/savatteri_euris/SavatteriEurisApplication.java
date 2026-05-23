package com.example.savatteri_euris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SavatteriEurisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SavatteriEurisApplication.class, args);
	}

}
