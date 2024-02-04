package ru.maksimov.imageserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ImageServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageServerApplication.class, args);
	}

}
