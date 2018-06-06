package com.gdn.recommendation.implementation;

import com.gdn.entity.*;
import com.gdn.recommendation.RecommendationService;
import com.gdn.repository.*;
import com.gdn.response.RecommendationResponse;
import com.gdn.response.SchedulingResponse;
import com.gdn.response.WebResponse;
import mapper.FleetRecommendationResponseMapper;
import mapper.RecommendationResponseMapper;
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
    public WebResponse<SchedulingResponse> executeBatch(String warehouseId) {
        boolean batchExecutionSuccessStatus;
        try {
            JobParameters fleetRecommendationJobParameters = new JobParametersBuilder()
                    .addLong(UUID.randomUUID().toString(),System.currentTimeMillis())
                    .addString("warehouse", warehouseId)
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(fleetRecommendationJob, fleetRecommendationJobParameters);
            batchExecutionSuccessStatus = !jobExecution.getStatus().isUnsuccessful();
            if(batchExecutionSuccessStatus){
                return WebResponse.OK(SchedulingResponse.builder()
                        .startTime(jobExecution.getStartTime())
                        .endTime(jobExecution.getEndTime())
                        .duration(jobExecution.getEndTime().getTime()-jobExecution.getStartTime().getTime())
                        .build());
            } else{
                return WebResponse.ERROR(jobExecution.getStatus().getBatchStatus().toString());
            }
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
            return WebResponse.ERROR(e.getMessage());
        }
    }

    @Override
    public int getResultRowCount(String warehouseId, Date pickupDate) {
        return recommendationRepository.getRowCount(Warehouse.builder()
                .id(warehouseId)
                .build(),
                pickupDate);
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
    public WebResponse<RecommendationResponse> findAllRecommendationFleetResult() {
        return WebResponse.OK(RecommendationResponseMapper.toRecommendationResponse(recommendationResultRepository.findAll()));
    }

    @Override
    public void choosePickupAndSendEmail(String recommendationResultId, Date pickupDate) {
        RecommendationResult recommendationResult = recommendationResultRepository.getOne(recommendationResultId);
        List<RecommendationFleet> recommendationFleetList = recommendationResult.getRecommendationFleetList();
        for (RecommendationFleet recommendationFleet:recommendationFleetList
             ) {
            String pickupId = "pickup_" + UUID.randomUUID().toString();
            pickupRepository.save(Pickup.builder()
                    .id(pickupId)
                    .pickupDate(pickupDate)
                    .fleet(recommendationFleet.getFleet())
                    .plateNumber("plate_number_" + UUID.randomUUID().toString())
                    .build());
            List<RecommendationDetail> recommendationDetailList = recommendationFleet.getRecommendationDetailList();
            for (RecommendationDetail recommendationDetail:recommendationDetailList
                 ) {
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
        recommendationResultRepository.deleteAll();
    }

}
