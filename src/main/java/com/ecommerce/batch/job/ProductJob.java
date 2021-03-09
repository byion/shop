package com.ecommerce.batch.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductJob {

    private final SimpleJobLauncher simpleJobLauncher;
    private final Job exportProductJob;


    private static final Logger log = LoggerFactory.getLogger(ProductJob.class);

    public ProductJob(SimpleJobLauncher simpleJobLauncher, Job exportProductJob) {
        this.simpleJobLauncher = simpleJobLauncher;
        this.exportProductJob = exportProductJob;
    }

    //    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    @Scheduled(initialDelay = 5000, fixedDelay = 120000)
    public void reportCurrentTime() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        log.info("The time is now {}", LocalDateTime.now());

        JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        JobExecution execution = simpleJobLauncher.run(exportProductJob, param);

        log.info("Job finished with status :" + execution.getStatus());
    }
}
