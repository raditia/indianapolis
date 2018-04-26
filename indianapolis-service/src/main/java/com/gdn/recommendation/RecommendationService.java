package com.gdn.recommendation;

import com.gdn.entity.RecommendationDetail;
import com.gdn.entity.RecommendationFleet;
import com.gdn.entity.RecommendationResult;

import java.util.List;

public interface RecommendationService {
    boolean executeBatch();
    int getResultRowCount();
    RecommendationResult saveRecommendationResult(RecommendationResult recommendationResult);
    RecommendationFleet saveRecommendationFleet(RecommendationFleet recommendationFleet);
    RecommendationDetail saveRecommendationDetail(RecommendationDetail recommendationDetail);
    List<RecommendationFleet> findAllRecommendationFleetResult();
}
