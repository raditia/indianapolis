package com.gdn.recommendation_algorithm;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Recommendation;

import java.util.List;

public interface RecommendationProcessorService {
    List<Recommendation> getThreeRecommendation(List<DatabaseQueryResult> resultList, String warehouseId);
}
