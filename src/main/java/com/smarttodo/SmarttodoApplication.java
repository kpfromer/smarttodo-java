package com.smarttodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SmarttodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmarttodoApplication.class, args);
	}
}
