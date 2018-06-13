package com.gdn.recommendation;

import com.gdn.entity.RecommendationDetail;
import com.gdn.entity.RecommendationFleet;
import com.gdn.entity.RecommendationResult;
import com.gdn.request.PickupChoiceRequest;
import com.gdn.response.FleetRecommendationResponse;
import com.gdn.response.RecommendationResponse;
import com.gdn.response.SchedulingResponse;
import com.gdn.response.WebResponse;

import java.util.Date;
import java.util.List;

public interface RecommendationService {
    void executeBatch();
    int getResultRowCount(String warehouseId, Date pickupDate);
    RecommendationResult saveRecommendationResult(RecommendationResult recommendationResult);
    RecommendationFleet saveRecommendationFleet(RecommendationFleet recommendationFleet);
    RecommendationDetail saveRecommendationDetail(RecommendationDetail recommendationDetail);
    WebResponse<RecommendationResponse> findAllRecommendationFleetResult();
    void choosePickupAndSendEmail(PickupChoiceRequest pickupChoiceRequest);
}
