package com.springbatch.controller;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springbatch.beans.ErrorResponseBean;
import com.springbatch.beans.KycSaveDataResponseBean;
import com.springbatch.exception.ErrorMessageHandling;
import com.springbatch.exception.PortalException;
import com.springbatch.services.BatchServices;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/kyc/queue")
@Api(value = "Batch Processing")
@CrossOrigin
public class BatchController {

	private static final Logger LOGGER = LogManager.getLogger(BatchController.class);

	@Autowired
    ErrorMessageHandling errorMessageHandling;

	@Autowired
	BatchServices batchServices;

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier("controllerJob")
	Job controllerJob;

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
	@ApiOperation(value = "To save the json in the Database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data saved successfully in the table.", response = KycSaveDataResponseBean.class),
            @ApiResponse(code = 400, message = "Error while saving data in the table.", response = ErrorResponseBean.class)
    })
	public KycSaveDataResponseBean saveData(@RequestBody String kycData, @RequestHeader Map<String, Object> headers) throws PortalException {
        try {
            return batchServices.saveData(kycData, headers.get("cif").toString());
        } catch (Exception e) {
            throw new PortalException("KYC101");
        }
    }

    @ExceptionHandler(PortalException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseBean> portalErrorException(PortalException ex) throws IOException {
        ErrorResponseBean portalErrorResponseBean = errorMessageHandling.errorHandlingResponse(ex.getErrCode());
        return new ResponseEntity<>(portalErrorResponseBean, HttpStatus.BAD_REQUEST);
    }
}


