package com.gdn.recommendation;

import com.gdn.entity.RecommendationDetail;
import com.gdn.entity.RecommendationFleet;
import com.gdn.entity.RecommendationResult;

import java.util.Date;
import java.util.List;

public interface RecommendationService {
    boolean executeBatch(String warehouseId);
    int getResultRowCount(String warehouseId);
    RecommendationResult saveRecommendationResult(RecommendationResult recommendationResult);
    RecommendationFleet saveRecommendationFleet(RecommendationFleet recommendationFleet);
    RecommendationDetail saveRecommendationDetail(RecommendationDetail recommendationDetail);
    List<RecommendationResult> findAllRecommendationFleetResult();
    void choosePickupAndSendEmail(String recommendationResultId, Date pickupDate);
}
