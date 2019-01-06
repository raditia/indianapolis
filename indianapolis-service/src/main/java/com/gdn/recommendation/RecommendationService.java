package com.gdn.recommendation;

import com.gdn.response.*;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

public interface RecommendationService {
    void executeBatch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException;
    WebResponse<RecommendationResponse> findAllRecommendationFleetResult(String warehouseId);
//    WebResponse<PickupChoiceResponse> saveChosenRecommendation(PickupChoiceRequest pickupChoiceRequest);
//    WebResponse<PickupChoiceResponse> choosePickupAndSendEmail(PickupChoiceRequest pickupChoiceRequest) throws MessagingException, IOException, DocumentException;
}
