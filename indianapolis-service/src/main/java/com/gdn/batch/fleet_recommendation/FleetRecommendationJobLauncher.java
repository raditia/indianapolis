package com.gdn.batch.fleet_recommendation;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FleetRecommendationJobLauncher {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job fleetRecommendationJob;

    @Scheduled(cron = "${recommendation.cron}")
    public void launchFleetRecommendationJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        jobLauncher.run(fleetRecommendationJob, jobParameters());
    }

    private JobParameters jobParameters() {
        return new JobParametersBuilder()
                .addLong("time",System.currentTimeMillis()).toJobParameters();
    }

}
