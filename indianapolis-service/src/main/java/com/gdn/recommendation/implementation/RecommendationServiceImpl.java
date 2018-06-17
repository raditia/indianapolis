package com.gdn.recommendation.implementation;

import com.gdn.email.SendEmailService;
import com.gdn.entity.*;
import com.gdn.pickup.PickupService;
import com.gdn.recommendation.RecommendationService;
import com.gdn.repository.*;
import com.gdn.request.PickupChoiceRequest;
import com.gdn.response.PickupChoiceResponse;
import com.gdn.response.RecommendationResponse;
import com.gdn.response.WebResponse;
import helper.DateHelper;
import mapper.PickupChoiceResponseMapper;
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
import org.springframework.transaction.annotation.Transactional;

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
    private CffRepository cffRepository;
    @Autowired
    private PickupService pickupService;
    @Autowired
    private SendEmailService sendEmailService;

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
    @Transactional
    public WebResponse<List<PickupChoiceResponse>> choosePickupAndSendEmail(PickupChoiceRequest pickupChoiceRequest) {
        RecommendationResult recommendationResult = recommendationResultRepository.getOne(pickupChoiceRequest.getRecommendationResultId());

        List<Pickup> pickupList = pickupService.savePickup(pickupChoiceRequest);
        List<PickupDetail> pickupDetailList = new ArrayList<>();
        pickupList.forEach(pickup -> pickupDetailList.addAll(pickup.getPickupDetailList()));

        String warehouseEmailAddress = sendEmailService.getWarehouseEmail(recommendationResult);
        LOGGER.info("Email warehouse : " + warehouseEmailAddress);

        String warehouseEmailContent = sendEmailService.getWarehouseEmailContent(recommendationResult.getWarehouse());
        LOGGER.info("Email warehouse content : \n" + warehouseEmailContent);

        List<String> merchantEmailAddressList = sendEmailService.getMerchantEmailList(pickupDetailList);
        merchantEmailAddressList.forEach(email -> LOGGER.info("Email merchant : " + email));
        List<Merchant> merchantList = sendEmailService.getMerchantList(pickupDetailList);
        for (Merchant merchant:merchantList
             ) {
            String merchantEmailContent = sendEmailService.getMerchantEmailContent(recommendationResult.getWarehouse(), merchant);
            LOGGER.info("Email merchant content : \n" + merchantEmailContent);
        }

        List<String> logisticVendorEmailAddressList = sendEmailService.getLogisticVendorEmailList(pickupList);
        logisticVendorEmailAddressList.forEach(email -> LOGGER.info("Email logistic : " + email));
        String logisticVendorEmailContent = sendEmailService.getLogisticVendorEmailContent(recommendationResult.getWarehouse());
        LOGGER.info("Email logistic vendor content : \n" + logisticVendorEmailContent);

        List<String> tpEmailAddressList = sendEmailService.getTpEmailList(pickupDetailList);
        tpEmailAddressList.forEach(email -> LOGGER.info("Email TP : " + email));
        List<User> tpList = sendEmailService.getTpList(pickupDetailList);
        for (User tp:tpList){
            String tpEmailContent = sendEmailService.getTpEmailContent(recommendationResult.getWarehouse(), tp);
            LOGGER.info("TP Email content : \n" + tpEmailContent);
        }

        recommendationResultRepository.deleteAllByWarehouse(recommendationResult.getWarehouse());
        return WebResponse.OK(PickupChoiceResponseMapper.toPickupChoiceResponseList(pickupList));
    }

}
