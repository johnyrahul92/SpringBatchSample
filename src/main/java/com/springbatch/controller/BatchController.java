package com.springbatch.controller;

import com.springbatch.beans.KycSaveDataResponse;
import com.springbatch.services.BatchServices;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.Map;

@RestController
@RequestMapping("/kyc/queue")
@Api(value = "Batch Processing")
@CrossOrigin
public class BatchController {

	private static final Logger LOGGER = LogManager.getLogger(BatchController.class);

	@Autowired
	BatchServices batchServices;

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

	@PostMapping("/saveData")
	@Consumes("Application/json")
	@Produces("Application/json")
	@ApiOperation(value = "To save the json in the Database", response = KycSaveDataResponse.class)
	public KycSaveDataResponse saveData(@RequestBody String kycData, @RequestHeader Map<String, Object> headers) {
		return batchServices.saveData(kycData, headers.get("cif").toString());
	}
}
