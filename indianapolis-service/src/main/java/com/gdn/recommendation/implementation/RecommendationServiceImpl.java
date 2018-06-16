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
    public WebResponse<RecommendationResponse> findAllRecommendationFleetResult(String warehouseId) {
        return WebResponse
                .OK(RecommendationResponseMapper.toRecommendationResponse(
                                recommendationResultRepository.findByWarehouse(
                                                Warehouse.builder()
                                                .id(warehouseId)
                                                .build())));
    }

    @Override
    public WebResponse<List<PickupChoiceResponse>> choosePickupAndSendEmail(PickupChoiceRequest pickupChoiceRequest) {
        RecommendationResult recommendationResult = recommendationResultRepository.getOne(pickupChoiceRequest.getRecommendationResultId());
        String warehouseEmailAddress = recommendationResult.getWarehouse().getEmailAddress();
        List<RecommendationFleet> recommendationFleetList = recommendationResult.getRecommendationFleetList();
        List<Pickup> pickupList = new ArrayList<>();
        List<PickupDetail> pickupDetailList;
        List<String> merchantEmailList = new ArrayList<>();
        for (RecommendationFleet recommendationFleet:recommendationFleetList
             ) {
            pickupDetailList = PickupDetailMapper.toPickupDetailList(recommendationFleet.getRecommendationDetailList());
            Pickup pickup = Pickup.builder()
                    .id("pickup_" + UUID.randomUUID().toString())
                    .pickupDate(pickupChoiceRequest.getPickupDate())
                    .fleet(recommendationFleet.getFleet())
                    .plateNumber("plate_number_" + UUID.randomUUID().toString())
                    .warehouse(recommendationResult.getWarehouse())
                    .pickupDetailList(pickupDetailList)
                    .build();
            pickupList.add(pickup);
            pickupRepository.save(pickup);
            for (String merchantEmail:getMerchantEmailList(pickupDetailList)
                 ) {
                if(!merchantEmailList.contains(merchantEmail)){
                    merchantEmailList.add(merchantEmail);
                }
            }
        }
        for (String email:merchantEmailList
             ) {
            LOGGER.info("Email merchant : " + email);
        }
        List<String> logisticVendorEmailAddressList = getLogisticVendorEmails(pickupList);
        for (String email:logisticVendorEmailAddressList
             ) {
            LOGGER.info("Email logistic vendor : " + email);
        }
        recommendationResultRepository.deleteAll();
        return WebResponse.OK(PickupChoiceResponseMapper.toPickupChoiceResponseList(pickupList));
    }

    private List<String> getLogisticVendorEmails(List<Pickup> pickupList){
        List<String> logisticVendorEmailList = new ArrayList<>();
        String logisticEmailAddress;
        for (Pickup pickup:pickupList
             ) {
            logisticEmailAddress = pickup.getFleet().getLogisticVendor().getEmailAddress();
            if(!logisticVendorEmailList.contains(logisticEmailAddress)) {
                logisticVendorEmailList.add(logisticEmailAddress);
            }
        }
        return logisticVendorEmailList;

    private List<String> getMerchantEmailList(List<PickupDetail> pickupDetailList){
        List<String> merchantEmailList = new ArrayList<>();
        String merchantEmailAddress;
        for (PickupDetail pickupDetail:pickupDetailList
             ) {
            merchantEmailAddress = pickupDetail.getMerchant().getEmailAddress();
            if(!merchantEmailList.contains(merchantEmailAddress)){
                merchantEmailList.add(merchantEmailAddress);
            }
        }
    }

}
