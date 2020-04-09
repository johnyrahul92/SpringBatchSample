package com.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.springbatch.tasks.RowsProcessor;
import com.springbatch.tasks.RowsReader;

@Configuration
@EnableScheduling
public class BatchConfig {
     
    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;
    
    @Autowired
	JobLauncher jobLauncher;
    
    @Autowired
    RowsReader rowsReader;
    
    @Autowired
    RowsProcessor rowsProcessor;
    
	@Autowired
	@Qualifier("demoJob")
	Job job;
    
    
    
    @Scheduled(cron = "0 */1 * * * ?")
	public void perform() throws Exception 
    {
        JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
        JobExecution jobExecution= jobLauncher.run(job, params);
    }
     
    @Bean
    public Step readRows(){
        return steps.get("readRows")
                .tasklet(rowsReader)
                .build();
    }  
    
    @Bean
    public Step processRows(){
        return steps.get("processRows")
                .tasklet(rowsProcessor)
                .build();
    } 
     
    @Bean
    public Job demoJob(){
        return jobs.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .start(readRows())
                .next(processRows())
                .build();
    }
    
    @Bean
    public Job controllerJob(){
        return jobs.get("controllerJob")
                .incrementer(new RunIdIncrementer())
                .start(readRows())
                //.next(stepTwo())
                .build();
    }
    
    
}