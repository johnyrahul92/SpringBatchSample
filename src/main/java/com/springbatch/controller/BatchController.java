package com.springbatch.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/v1")
@Api(value = "Batch Processing")
public class BatchController {

	private static final Logger LOGGER = LogManager.getLogger(BatchController.class);

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier("controllerJob")
	Job controllerJob;

	@GetMapping("/test")
	public String testCall() {
		return "Hello world";
	}
	
	@GetMapping("/test1")
	public String testCall1() {
		return "Hello world1";
	}

	@GetMapping("/startJob")
	public String executeJob() throws Exception {

		try {

			JobParameters params = new JobParametersBuilder().addString("JobID", "From Controller").toJobParameters();

			JobExecution jobExecution = jobLauncher.run(controllerJob, params);

			if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
				LOGGER.info("Success");
			} else {
				LOGGER.warn("error batch status failed");

			}

		} catch (Exception e) {

			LOGGER.error(e.getMessage()); // throw new
	

		}
		
		return "Started Job";

	}

}
