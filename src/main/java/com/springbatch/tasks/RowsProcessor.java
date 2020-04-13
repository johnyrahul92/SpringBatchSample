package com.springbatch.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.springbatch.beans.KycRequestBean;
import com.springbatch.entity.KycCustomerData;
import com.springbatch.services.BatchServices;

@Component
public class RowsProcessor implements Tasklet, StepExecutionListener {

	private static final Logger LOGGER = LogManager.getLogger(RowsProcessor.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private List<KycCustomerData> rows;

	@Autowired
	BatchServices batchServices;
	
	@SuppressWarnings("unchecked")
	@Override
    public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution
		          .getJobExecution()
		          .getExecutionContext();
		        this.rows = (List<KycCustomerData>)executionContext.get("rows");
		LOGGER.debug("The time is now {}", dateFormat.format(new Date()));
		LOGGER.debug("RowsProcessor initialized.");
    }
	 
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception 
    {
        LOGGER.debug("-------------------START-------------------------");
        final String uri = "http://localhost:8080/esb/initiate";
        for(KycCustomerData row:rows) {
        	KycRequestBean kycRequestBean = new KycRequestBean();
        	kycRequestBean.setUniqueId(row.getId());
        	kycRequestBean.setKycData(row.getKycData());
        	
        	RestTemplate restTemplate = new RestTemplate();
        	LOGGER.info("Call Rest endpoint with : "+kycRequestBean.toString());
    	    String result = restTemplate.postForObject(uri, kycRequestBean ,String.class);
    	    
    	    LOGGER.info("Response from ESB call : "+result.toString());
    	    
        	//TODO: Update status and increment counter
        }
        LOGGER.debug("-------------------END-------------------------");
        return RepeatStatus.FINISHED;
    }  
    
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
    	LOGGER.debug("RowsProcessor ended.");
    	this.rows = null;
        return ExitStatus.COMPLETED;
    }
}