package com.example.demo;

import com.google.common.eventbus.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DemoApplication {

	EventBus eventBus;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
