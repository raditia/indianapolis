package com.gdn.recommendation.implementation;

import com.gdn.entity.*;
import com.gdn.recommendation.RecommendationService;
import com.gdn.repository.*;
import com.gdn.response.RecommendationResponse;
import com.gdn.response.WebResponse;
import com.gdn.mapper.RecommendationResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job fleetRecommendationJob;
    @Autowired
    private RecommendationRepository recommendationRepository;
    @Autowired
    private RecommendationResultRepository recommendationResultRepository;
    @Autowired
    private CffRepository cffRepository;

    @Scheduled(cron = "${recommendation.cron}")
    @Override
    public void executeBatch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        List<Warehouse> warehouseListOfCffPickedUpdTomorrow = cffRepository.findDistinctWarehouse();
        for (Warehouse warehouse:warehouseListOfCffPickedUpdTomorrow
             ) {
            int rowCount = recommendationRepository.getRowCount(warehouse);
            LOGGER.info("Warehouse ID listed on cff to be pickupFleet tomorrow : " + warehouse.getId());
            JobParameters fleetRecommendationJobParameters = new JobParametersBuilder()
                    .addLong(UUID.randomUUID().toString(),System.currentTimeMillis())
                    .addString("warehouse", warehouse.getId())
                    .addString("rowCount", String.valueOf(rowCount))
                    .toJobParameters();
            jobLauncher.run(fleetRecommendationJob, fleetRecommendationJobParameters);
        }
    }

    @Override
    public WebResponse<RecommendationResponse> findAllRecommendationFleetResult(String warehouseId) {
        return WebResponse
                .OK(RecommendationResponseMapper.toRecommendationResponse(
                                recommendationResultRepository.findByWarehouse(
                                                Warehouse.builder()
                                                .id(warehouseId)
                                                .build())));
    }

}
