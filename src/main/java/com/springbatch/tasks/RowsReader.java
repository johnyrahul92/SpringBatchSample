package com.springbatch.tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springbatch.entity.KycCustomerData;
import com.springbatch.services.BatchServices;

@Component
public class RowsReader implements Tasklet, StepExecutionListener {

	private static final Logger LOGGER = LogManager.getLogger(RowsReader.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private List<KycCustomerData> rows;

	@Autowired
	BatchServices batchServices;
	
	@Override
    public void beforeStep(StepExecution stepExecution) {
		rows = new ArrayList<>();
		LOGGER.info("The time is now {}", dateFormat.format(new Date()));
		LOGGER.info("RowsReader initialized.");
    }
	 
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception 
    {
        rows = batchServices.getData();
        for(KycCustomerData row:rows) {
        	LOGGER.info(row.toString());
        }
        return RepeatStatus.FINISHED;
    }  
    
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
    	stepExecution
        .getJobExecution()
        .getExecutionContext()
        .put("rows", this.rows);
    	LOGGER.info("RowsReader ended.");
        return ExitStatus.COMPLETED;
    }
}