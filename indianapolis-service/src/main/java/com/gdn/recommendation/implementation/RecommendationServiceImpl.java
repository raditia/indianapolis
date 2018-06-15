package com.gdn.recommendation.implementation;

import com.gdn.entity.*;
import com.gdn.recommendation.RecommendationService;
import com.gdn.repository.*;
import com.gdn.request.PickupChoiceRequest;
import com.gdn.response.PickupChoiceResponse;
import com.gdn.response.RecommendationResponse;
import com.gdn.response.SchedulingResponse;
import com.gdn.response.WebResponse;
import helper.DateHelper;
import mapper.PickupChoiceResponseMapper;
import mapper.PickupDetailMapper;
import mapper.RecommendationResponseMapper;
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

import java.util.ArrayList;
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
    @Autowired
    private CffRepository cffRepository;

    @Scheduled(cron = "${recommendation.cron}")
    @Override
    public void executeBatch() {
        boolean batchExecutionSuccessStatus;
        List<Warehouse> warehouseListOfCffPickedUpdTomorrow = cffRepository.findDistinctWarehouseAndPickupDateIs(DateHelper.tomorrow());
        for (Warehouse warehouse:warehouseListOfCffPickedUpdTomorrow
             ) {
            LOGGER.info("Warehouse ID listed on cff to be pickup tomorrow : " + warehouse.getId());
            try {
                JobParameters fleetRecommendationJobParameters = new JobParametersBuilder()
                        .addLong(UUID.randomUUID().toString(),System.currentTimeMillis())
                        .addString("warehouse", warehouse.getId())
                        .toJobParameters();
                JobExecution jobExecution = jobLauncher.run(fleetRecommendationJob, fleetRecommendationJobParameters);
                batchExecutionSuccessStatus = !jobExecution.getStatus().isUnsuccessful();
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
                e.printStackTrace();
            }
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
    public WebResponse<List<PickupChoiceResponse>> choosePickupAndSendEmail(PickupChoiceRequest pickupChoiceRequest) {
        RecommendationResult recommendationResult = recommendationResultRepository.getOne(pickupChoiceRequest.getRecommendationResultId());
        List<RecommendationFleet> recommendationFleetList = recommendationResult.getRecommendationFleetList();
        List<Pickup> pickupList = new ArrayList<>();
        for (RecommendationFleet recommendationFleet:recommendationFleetList
             ) {
            Pickup pickup = Pickup.builder()
                    .id("pickup_" + UUID.randomUUID().toString())
                    .pickupDate(pickupChoiceRequest.getPickupDate())
                    .fleet(recommendationFleet.getFleet())
                    .plateNumber("plate_number_" + UUID.randomUUID().toString())
                    .pickupDetailList(PickupDetailMapper.toPickupDetailList(recommendationFleet.getRecommendationDetailList()))
                    .build();
            pickupList.add(pickup);
            pickupRepository.save(pickup);
        }
        recommendationResultRepository.deleteAll();
        return WebResponse.OK(PickupChoiceResponseMapper.toPickupChoiceResponseList(pickupList));
    }

}
