package com.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;



@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchSampleApplication extends SpringBootServletInitializer {
	
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBatchSampleApplication.class, args);
	}
	
	

}
