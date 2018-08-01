package com.gdn.util;

import com.gdn.entity.RecommendationFleet;

import java.util.ArrayList;
import java.util.List;

public class RecommendationFleetUtil {
    public static RecommendationFleet recommendationFleetMinusRecommendationResult = RecommendationFleet.builder()
            .id("id")
            .fleet(FleetUtil.fleetMotorCompleteAttribute)
            .fleetSkuPickupQty(0)
            .fleetCbmPickupAmount(0.0f)
            .recommendationDetailList(RecommendationDetailUtil.recommendationDetailListMinusRecommendationFleet)
            .build();
    public static List<RecommendationFleet> recommendationFleetListMinusRecommendationResult = new ArrayList<RecommendationFleet>(){{
        add(recommendationFleetMinusRecommendationResult);
    }};
}
