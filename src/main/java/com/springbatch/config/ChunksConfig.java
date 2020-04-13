package com.springbatch.config;


import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.springbatch.entity.KycCustomerData;
import com.springbatch.tasks.RowsProcessorChunk;
import com.springbatch.tasks.RowsWriterChunk;

@Configuration
@EnableBatchProcessing
public class ChunksConfig {

	private static final Logger LOGGER = LogManager.getLogger(ChunksConfig.class);
 
    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;
    
    @Autowired
	JobLauncher jobLauncher;
    
    @Autowired
    DataSource dataSource;
    
    @Autowired
	@Qualifier("chunksJob")
	Job job;
    
    @Value("${esb.retry.attempt.limit}")
    private Long attemptLimit;
    
    @Value("${esb.retry.filter.status}")
    private String filterStatus;
    
    //@Scheduled(cron = "0 */1 * * * ?")
	public void perform() throws Exception 
    {
    	LOGGER.debug("Going to perform chunk job");
        JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .addString("filterStatus", filterStatus)
                    .addLong("attemptLimit", attemptLimit)
                    .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, params);
    }
    
    
    @Bean(destroyMethod="")
    @StepScope
    public JdbcCursorItemReader<KycCustomerData> itemReader(@Value("#{jobParameters['filterStatus']}") String filterStatus, @Value("#{jobParameters['attemptLimit']}") Long attemptLimit ) {
    	JdbcCursorItemReader<KycCustomerData> dataJdbcCursorItemReader = new JdbcCursorItemReader<>();
        dataJdbcCursorItemReader.setSql("select ID,KYCDATA,CIF,COUNT,STATUS,CREATEDON,UPDATEDON from kyccustomerdatatable where status='"+filterStatus+"' and count<"+attemptLimit);
        dataJdbcCursorItemReader.setDataSource(dataSource);
        dataJdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<>(KycCustomerData.class));
        return dataJdbcCursorItemReader;
    	
    }
 
    @Bean
    public ItemProcessor<KycCustomerData, KycCustomerData> itemProcessor() {
        return new RowsProcessorChunk();
    }
 
    @Bean
    public ItemWriter<KycCustomerData> itemWriter() {
        return new RowsWriterChunk();
    }
 
    @Bean
    protected Step processRowsChunk(ItemReader<KycCustomerData> reader,
      ItemProcessor<KycCustomerData, KycCustomerData> processor, ItemWriter<KycCustomerData> writer) {
        return steps.get("processRowsChunk").<KycCustomerData, KycCustomerData> chunk(2)
          .reader(reader)
          .processor(processor)
          .writer(writer)
          .build();
    }
 
    @Bean
    public Job chunksJob() {
        return jobs
          .get("chunksJob")
          .start(processRowsChunk(itemReader(null,null), itemProcessor(), itemWriter()))
          .build();
    }
 
}