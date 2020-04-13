package com.springbatch.tasks;

import com.springbatch.beans.KycRequestBean;
import com.springbatch.entity.KycCustomerData;
import com.springbatch.services.BatchServices;
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
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class KycBatchProcessor implements Tasklet, StepExecutionListener {

    private static final Logger LOGGER = LogManager.getLogger(KycBatchProcessor.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    BatchServices batchServices;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.debug("The time is now {}", dateFormat.format(new Date()));
        LOGGER.debug("RowsReader initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        List<KycCustomerData> rows = readRows(); //method to read the rows
        callESBService(rows); //method to call esb services
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.debug("RowsProcessor ended.");
        return ExitStatus.COMPLETED;
    }

    private void callESBService(List<KycCustomerData> rows) {
        LOGGER.debug("-------------------START ESB-------------------------");
        final String uri = "http://localhost:8080/esb/initiate";
        for(KycCustomerData row:rows) {
            KycRequestBean kycRequestBean = new KycRequestBean();
            kycRequestBean.setUniqueId(row.getId());
            kycRequestBean.setKycData(row.getKycData());

            RestTemplate restTemplate = new RestTemplate();
            LOGGER.info("Call Rest endpoint with : "+kycRequestBean.toString());
            String result = restTemplate.postForObject(uri, kycRequestBean ,String.class);

            LOGGER.info("Response from ESB call : "+result);
        }
        LOGGER.debug("-------------------END ESB-------------------------");
    }

    List<KycCustomerData> readRows() throws Exception {
        List<KycCustomerData> rows = batchServices.getKycFilteredData();
        LOGGER.debug("-------------------START-------------------------");
        LOGGER.info("Rows read");
        for(KycCustomerData row:rows) {
            LOGGER.info(row.toString());
        }
        LOGGER.debug("-------------------END-------------------------");
        return rows;
    }
}
