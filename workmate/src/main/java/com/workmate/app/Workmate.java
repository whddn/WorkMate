package com.workmate.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.yedam.app.**.mapper")
public class Workmate {

	public static void main(String[] args) {
		SpringApplication.run(Workmate.class, args);
	}

}
