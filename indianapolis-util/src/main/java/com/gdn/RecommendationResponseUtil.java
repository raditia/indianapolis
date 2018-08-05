package com.gdn;

import com.gdn.response.RecommendationResponse;

public class RecommendationResponseUtil {
    public static RecommendationResponse recommendationResponseCompleteAttribute = RecommendationResponse.builder()
            .cbmTotal(RecommendationResultUtil.recommendationResultCompleteAttribute.getTotalCbm())
            .warehouseName(RecommendationResultUtil.recommendationResultCompleteAttribute.getWarehouse().getAddress())
            .fleetRecommendationResponseList(FleetRecommendationResponseUtil.fleetRecommendationResponseListCompleteAttribute)
            .build();
}
