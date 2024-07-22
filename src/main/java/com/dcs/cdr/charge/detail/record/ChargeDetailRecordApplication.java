package com.dcs.cdr.charge.detail.record;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChargeDetailRecordApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChargeDetailRecordApplication.class, args);
	}

}
