package com.softplan.jpm.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages="com.softplan")
@EnableJpaRepositories("com.softplan.jpm.jpa.repository")
@EntityScan("com.softplan.jpm.entities")
public class GestorProcessosJudiciaisApplication {
	
	private static final Logger log = LoggerFactory.getLogger(GestorProcessosJudiciaisApplication.class);

	public static void main(String[] args) {
		 log.info("StartApplication Judicial Process REST API.."); 
		SpringApplication.run(GestorProcessosJudiciaisApplication.class, args);
	}
}
