package com.gdn.batch.fleet_recommendation;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class FleetRecommendationJobExecutionListener implements JobExecutionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FleetRecommendationJobExecutionListener.class);

    private DateTime startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = new DateTime(jobExecution.getStartTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        DateTime stopTime = new DateTime(jobExecution.getEndTime());
        LOGGER.info("Total time taken : " + getTimeInMillis(startTime, stopTime) + " ms");
    }

    private long getTimeInMillis(DateTime start, DateTime stop){
        return stop.getMillis() - start.getMillis();
    }
}
