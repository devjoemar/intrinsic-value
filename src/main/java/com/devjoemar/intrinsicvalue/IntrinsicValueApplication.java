package com.devjoemar.intrinsicvalue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class IntrinsicValueApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntrinsicValueApplication.class, args);
	}

}
