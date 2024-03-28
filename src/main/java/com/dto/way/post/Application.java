package com.dto.way.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)	//	datasource 연결시 지울 것
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
