package com.gdn.recommendation;

import com.gdn.response.*;

public interface RecommendationService {
    void executeBatch();
    WebResponse<RecommendationResponse> findAllRecommendationFleetResult(String warehouseId);
//    WebResponse<PickupChoiceResponse> saveChosenRecommendation(PickupChoiceRequest pickupChoiceRequest);
//    WebResponse<PickupChoiceResponse> choosePickupAndSendEmail(PickupChoiceRequest pickupChoiceRequest) throws MessagingException, IOException, DocumentException;
}
