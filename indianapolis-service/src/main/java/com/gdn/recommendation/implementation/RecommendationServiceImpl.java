package com.gdn.recommendation.implementation;

import com.gdn.entity.*;
import com.gdn.recommendation.RecommendationService;
import com.gdn.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private RecommendationFleetRepository recommendationFleetRepository;
    @Autowired
    private RecommendationDetailRepository recommendationDetailRepository;
    @Autowired
    private PickupRepository pickupRepository;
    @Autowired
    private PickupDetailRepository pickupDetailRepository;

    @Override
    public boolean executeBatch(String warehouseId) {
        boolean batchExecutionSuccessStatus=false;
        try {
            JobParameters fleetRecommendationJobParameters = new JobParametersBuilder()
                    .addLong(UUID.randomUUID().toString(),System.currentTimeMillis())
                    .addString("warehouse", warehouseId)
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(fleetRecommendationJob, fleetRecommendationJobParameters);
            batchExecutionSuccessStatus = !jobExecution.getStatus().isUnsuccessful();
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        return batchExecutionSuccessStatus;
    }

    @Override
    public int getResultRowCount(String warehouseId) {
        return recommendationRepository.getRowCount(Warehouse.builder()
                .id(warehouseId)
                .build());
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

    @Override
    public List<RecommendationFleet> findAllRecommendationFleetResult() {
        return recommendationFleetRepository.findAll();
    }

    @Override
    public void choosePickupAndSendEmail(String recommendationFleetId, Date pickupDate) {
        List<RecommendationDetail> recommendationDetailList = recommendationDetailRepository.findAllByRecommendationFleetId(recommendationFleetId);
        for (RecommendationDetail recommendationDetail:recommendationDetailList
             ) {
            String pickupId = "pickup_" + UUID.randomUUID().toString();
            pickupRepository.save(Pickup.builder()
                    .id(pickupId)
                    .pickupDate(pickupDate)
                    .fleet(recommendationDetail.getRecommendationFleet().getFleet())
                    .build());
            pickupDetailRepository.save(PickupDetail.builder()
                    .id("pickup_detail_" + UUID.randomUUID().toString())
                    .pickup(Pickup.builder()
                            .id(pickupId)
                            .build())
                    .cbmPickupAmount(recommendationDetail.getCbmPickupAmount())
                    .skuPickupQuantity(recommendationDetail.getSkuPickupQty())
                    .merchant(recommendationDetail.getMerchant())
                    .sku(recommendationDetail.getSku())
                    .pickupPoint(recommendationDetail.getPickupPoint())
                    .build());
        }
    }

}
