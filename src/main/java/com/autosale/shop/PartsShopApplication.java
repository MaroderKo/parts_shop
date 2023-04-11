package com.autosale.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PartsShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartsShopApplication.class, args);
	}

}
