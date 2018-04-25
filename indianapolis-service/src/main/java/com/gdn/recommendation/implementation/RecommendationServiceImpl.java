package com.gdn.recommendation.implementation;

import com.gdn.entity.RecommendationDetail;
import com.gdn.entity.RecommendationFleet;
import com.gdn.entity.RecommendationResult;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.RecommendationService;
import com.gdn.repository.RecommendationDetailRepository;
import com.gdn.repository.RecommendationFleetRepository;
import com.gdn.repository.RecommendationRepository;
import com.gdn.repository.RecommendationResultRepository;
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
    @Autowired
    private RecommendationResultRepository recommendationResultRepository;
    @Autowired
    private RecommendationFleetRepository recommendationFleetRepository;
    @Autowired
    private RecommendationDetailRepository recommendationDetailRepository;

    private List<DatabaseQueryResult> pickupList = new ArrayList<>();

    @Override
    public List<DatabaseQueryResult> executeBatch() {
        try {
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
        return recommendationRepository.getRowCount();
    }

    @Override
    public RecommendationResult saveRecommendationResult(RecommendationResult recommendationResult) {
        return recommendationResultRepository.save(recommendationResult);
    }

    @Override
    public RecommendationFleet saveRecommendationFleet(RecommendationFleet recommendationFleet) {
        return recommendationFleetRepository.save(recommendationFleet);
    }

    @Override
    public RecommendationDetail saveRecommendationDetail(RecommendationDetail recommendationDetail) {
        return recommendationDetailRepository.save(recommendationDetail);
    }
}
