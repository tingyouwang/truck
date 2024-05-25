package com.luzhu.truck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TruckApplication {

	public static void main(String[] args) {
		SpringApplication.run(TruckApplication.class, args);
	}

}
