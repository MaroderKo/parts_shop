package com.autosale.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class PartsShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartsShopApplication.class, args);
	}

}
