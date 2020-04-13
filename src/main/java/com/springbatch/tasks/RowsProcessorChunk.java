package com.springbatch.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.web.client.RestTemplate;

import com.springbatch.beans.KycRequestBean;
import com.springbatch.entity.KycCustomerData;

public class RowsProcessorChunk implements ItemProcessor<KycCustomerData, KycCustomerData>, StepExecutionListener {

	private static final Logger LOGGER = LogManager.getLogger(RowsProcessorChunk.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.debug("The time is now {}", dateFormat.format(new Date()));
		LOGGER.debug("Going to process rows for chunk");
        LOGGER.debug("-------------------START-------------------------");
    }

	@Override
	public KycCustomerData process(KycCustomerData row) throws Exception {
		final String uri = "http://localhost:8080/esb/initiate";
    	KycRequestBean kycRequestBean = new KycRequestBean();
    	kycRequestBean.setUniqueId(row.getId());
    	kycRequestBean.setKycData(row.getKycData());
    	
    	RestTemplate restTemplate = new RestTemplate();
    	LOGGER.info("Call Rest endpoint with : "+kycRequestBean.toString());
	    String result = restTemplate.postForObject(uri, kycRequestBean ,String.class);
	    
	    LOGGER.info("Response from ESB call : "+result.toString());
	    
    	//TODO: Update status and increment counter
		return row;
	}

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.debug("-------------------END-------------------------");
    	LOGGER.debug("Processing completed");
        return ExitStatus.COMPLETED;
    }
}
