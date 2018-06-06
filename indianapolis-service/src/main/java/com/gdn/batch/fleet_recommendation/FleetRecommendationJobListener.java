package com.gdn.batch.fleet_recommendation;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FleetRecommendationJobListener implements JobExecutionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FleetRecommendationJobListener.class);

    private DateTime startTime, stopTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = new DateTime();
        LOGGER.info("Job starts at " + startTime.getHourOfDay() + ":" + startTime.getMinuteOfHour());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        stopTime = new DateTime();
        LOGGER.info("Job stops at " + stopTime.getHourOfDay() + ":" + startTime.getMinuteOfHour());
        LOGGER.info("Total time taken in millis : " + getTimeInMillis(startTime, stopTime) + " ms");

        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("Job Completed!");
        } else {
            LOGGER.error("Job not completed!");
            List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
            for (Throwable th : exceptionList){
                LOGGER.error("Exception error " + th.getLocalizedMessage());
            }
        }
    }

    private long getTimeInMillis(DateTime start, DateTime stop){
        return stop.getMillis() - start.getMillis();
    }
}
