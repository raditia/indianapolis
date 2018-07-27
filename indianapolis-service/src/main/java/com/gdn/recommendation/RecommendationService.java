package com.gdn.recommendation;

import com.gdn.entity.RecommendationDetail;
import com.gdn.entity.RecommendationFleet;
import com.gdn.entity.RecommendationResult;
import com.gdn.request.PickupChoiceRequest;
import com.gdn.response.*;
import com.itextpdf.text.DocumentException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface RecommendationService {
    void executeBatch();
    RecommendationResult saveRecommendationResult(RecommendationResult recommendationResult);
    RecommendationFleet saveRecommendationFleet(RecommendationFleet recommendationFleet);
    RecommendationDetail saveRecommendationDetail(RecommendationDetail recommendationDetail);
    WebResponse<RecommendationResponse> findAllRecommendationFleetResult(String warehouseId);
    WebResponse<List<PickupChoiceResponse>> choosePickupAndSendEmail(PickupChoiceRequest pickupChoiceRequest) throws MessagingException, IOException, DocumentException;
}
