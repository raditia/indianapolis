package com.gdn.recommendation.implementation;

import com.gdn.SchedulingStatus;
import com.gdn.email.Email;
import com.gdn.email.SendEmailService;
import com.gdn.entity.*;
import com.gdn.pickup.PickupService;
import com.gdn.recommendation.RecommendationService;
import com.gdn.repository.*;
import com.gdn.request.PickupChoiceRequest;
import com.gdn.response.PickupChoiceResponse;
import com.gdn.response.RecommendationResponse;
import com.gdn.response.WebResponse;
import com.gdn.helper.DateHelper;
import com.gdn.mapper.PickupChoiceResponseMapper;
import com.gdn.mapper.RecommendationResponseMapper;
import com.itextpdf.text.DocumentException;
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private CffRepository cffRepository;
    @Autowired
    private PickupService pickupService;
    @Autowired
    private SendEmailService sendEmailService;

    @Scheduled(cron = "${recommendation.cron}")
    @Override
    public void executeBatch() {
        List<Warehouse> warehouseListOfCffPickedUpdTomorrow = cffRepository.findDistinctWarehouseAndPickupDate(DateHelper.tomorrow());
        for (Warehouse warehouse:warehouseListOfCffPickedUpdTomorrow
             ) {
            int rowCount = recommendationRepository.getRowCount(warehouse, DateHelper.tomorrow());
            LOGGER.info("Warehouse ID listed on cff to be pickup tomorrow : " + warehouse.getId());
            try {
                JobParameters fleetRecommendationJobParameters = new JobParametersBuilder()
                        .addLong(UUID.randomUUID().toString(),System.currentTimeMillis())
                        .addString("warehouse", warehouse.getId())
                        .addString("rowCount", String.valueOf(rowCount))
                        .toJobParameters();
                jobLauncher.run(fleetRecommendationJob, fleetRecommendationJobParameters);
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
                e.printStackTrace();
            }
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

    @Override
    public WebResponse<List<PickupChoiceResponse>> choosePickupAndSendEmail(PickupChoiceRequest pickupChoiceRequest) throws MessagingException, IOException, DocumentException {
        RecommendationResult recommendationResult = recommendationResultRepository.getOne(pickupChoiceRequest.getRecommendationResultId());

        List<Pickup> pickupList = pickupService.savePickup(pickupChoiceRequest);
//        List<PickupDetail> pickupDetailList = new ArrayList<>();
//        pickupList.forEach(pickup -> pickupDetailList.addAll(pickup.getPickupDetailList()));
//
//        SimpleDateFormat pickupDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
//        String pickupDate = pickupDateFormatter.format(pickupChoiceRequest.getPickupDate());
//
//        sendEmailToWarehouse(recommendationResult, pickupDate, pickupList);
//        sendEmailToMerchant(recommendationResult, pickupDate, pickupDetailList);
//        sendEmailToLogisticVendor(recommendationResult, pickupDate, pickupList);
//        sendEmailToTradePartnership(recommendationResult, pickupDate, pickupDetailList);
        recommendationResultRepository.deleteAllByWarehouse(recommendationResult.getWarehouse());
        return WebResponse.OK(PickupChoiceResponseMapper.toPickupChoiceResponseList(pickupList));
    }

    private void sendEmailToWarehouse(RecommendationResult recommendationResult,
                                      String pickupDate,
                                      List<Pickup> pickupList) throws DocumentException, MessagingException, IOException {
        sendEmailService
                .sendEmail(Email.builder()
                        .emailAddressDestination(sendEmailService
                                .getWarehouseEmail(recommendationResult))
                        .emailSubject("Fulfillment by Blibli - warehouse")
                        .emailBodyContextList(sendEmailService
                                .getWarehouseEmailContent(
                                        recommendationResult.getWarehouse(),
                                        pickupDate,
                                        pickupList))
                        .pickupDate(pickupDate)
                        .build());
    }

    private void sendEmailToMerchant(RecommendationResult recommendationResult,
                                     String pickupDate,
                                     List<PickupDetail> pickupDetailList) throws DocumentException, MessagingException, IOException {
        List<Merchant> merchantList = sendEmailService.getMerchantList(pickupDetailList);
        for (Merchant merchant:merchantList
                ) {
            sendEmailService
                    .sendEmail(Email.builder()
                            .emailAddressDestination(merchant.getEmailAddress())
                            .emailSubject("Fulfillment by Blibli - merchant - koli label")
                            .emailBodyText("Hai " + merchant.getName() + "!\n" +
                                    "Barang anda akan diambil oleh blibli pada " + pickupDate + "\n" +
                                    "Harap cetak koli label ini dan tempelkan pada barang anda sesuai dengan data yang tertera.\n" +
                                    "Terima kasih")
                            .emailBodyContextList(sendEmailService
                                    .getMerchantEmailContent(
                                            recommendationResult.getWarehouse(),
                                            merchant))
                            .pickupDate(pickupDate)
                            .build());
        }
    }

    private void sendEmailToLogisticVendor(RecommendationResult recommendationResult,
                                           String pickupDate,
                                           List<Pickup> pickupList) throws DocumentException, MessagingException, IOException {
        List<LogisticVendor> logisticVendorList = sendEmailService.getLogisticVendorList(pickupList);
        for (LogisticVendor logisticVendor:logisticVendorList){
            sendEmailService
                    .sendEmail(Email.builder()
                            .emailAddressDestination(logisticVendor.getEmailAddress())
                            .emailSubject("Fulfillment by Blibli - logistic vendor")
                            .emailBodyContextList(sendEmailService
                                    .getLogisticVendorEmailContent(
                                            recommendationResult.getWarehouse(),
                                            logisticVendor,
                                            pickupDate))
                            .pickupDate(pickupDate)
                            .build());
        }
    }

    private void sendEmailToTradePartnership(RecommendationResult recommendationResult,
                                             String pickupDate,
                                             List<PickupDetail> pickupDetailList) throws DocumentException, MessagingException, IOException {
        List<User> tpList = sendEmailService.getTpList(pickupDetailList);
        for (User tp:tpList){
            sendEmailService
                    .sendEmail(Email.builder()
                            .emailAddressDestination(tp.getEmailAddress())
                            .emailSubject("Fulfillment by Blibli - trade partnership - koli label")
                            .emailBodyText("Hai " + tp.getName() + "!\n" +
                                    "Berikut adalah koli label dari para merchant yang anda tangani.\n" +
                                    "Barang merchant akan diangkut pada " + pickupDate)
                            .emailBodyContextList(sendEmailService
                                    .getTpEmailContent(
                                            recommendationResult.getWarehouse(),
                                            tp))
                            .pickupDate(pickupDate)
                            .build());
        }
    }

}
