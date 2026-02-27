package com.zosh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcommerceMultiVendorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceMultiVendorApplication.class, args);
	}

}
