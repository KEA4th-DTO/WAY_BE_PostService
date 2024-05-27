package com.dto.way.post;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;


@EnableJpaAuditing // JPA Auditing 활성화
@SpringBootApplication()
@EnableFeignClients
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println(org.hibernate.Version.getVersionString());
	}

	@PostConstruct
	public void init() {
		// timezone 설정
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		System.out.println("Current TimeZone: " + TimeZone.getDefault().getID());
	}
}
