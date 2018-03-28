package com.gdn.recommendation.implementation;

import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.RecommendationService;
import com.gdn.repository.RecommendationRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job fleetRecommendationJob;
    @Autowired
    private RecommendationRepository recommendationRepository;
    private List<DatabaseQueryResult> pickupList = new ArrayList<>();
    private int resultRowCount=0;

    @Override
    public List<DatabaseQueryResult> executeBatch() {
        try {
            resultRowCount = recommendationRepository.getRowCount();
            JobParameters fleetRecommendationJobParameters = new JobParametersBuilder()
                    .addLong("time",System.currentTimeMillis()).toJobParameters();
            jobLauncher.run(fleetRecommendationJob, fleetRecommendationJobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        return this.pickupList;
    }

    @Override
    public List<DatabaseQueryResult> setPickupList(List<DatabaseQueryResult> pickupList) {
        return this.pickupList = pickupList;
    }

    @Override
    public int getResultRowCount() {
        return this.resultRowCount;
    }
}
