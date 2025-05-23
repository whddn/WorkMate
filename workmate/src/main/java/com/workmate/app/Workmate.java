package com.workmate.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync 
@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "com.workmate.app.**.mapper")
public class Workmate {
	
	public static void main(String[] args) {
		SpringApplication.run(Workmate.class, args);
	}

}
