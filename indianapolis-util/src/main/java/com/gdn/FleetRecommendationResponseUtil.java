package com.gdn;

import com.gdn.entity.RecommendationFleet;
import com.gdn.response.FleetRecommendationResponse;

import java.util.ArrayList;
import java.util.List;

public class FleetRecommendationResponseUtil {
    public static FleetRecommendationResponse fleetRecommendationResponseUtilCompleteAttribute = FleetRecommendationResponse.builder()
            .id(RecommendationResultUtil.recommendationResultCompleteAttribute.getId())
            .fleetName(getFleetNameList())
            .build();
    private static List<String> getFleetNameList(){
        List<String> fleetNameList = new ArrayList<>();
        for (RecommendationFleet fleet : RecommendationResultUtil.recommendationResultCompleteAttribute.getRecommendationFleetList()){
            fleetNameList.add(fleet.getFleet().getName());
        }
        return fleetNameList;
    }
    public static List<FleetRecommendationResponse> fleetRecommendationResponseListCompleteAttribute = new ArrayList<FleetRecommendationResponse>(){{
        add(fleetRecommendationResponseUtilCompleteAttribute);
    }};
}
