package com.gdn.mapper;

import com.gdn.entity.RecommendationFleet;
import com.gdn.entity.RecommendationResult;
import com.gdn.response.FleetRecommendationResponse;

import java.util.ArrayList;
import java.util.List;

public class FleetRecommendationResponseMapper {

    public static FleetRecommendationResponse toRecommendationResponse(RecommendationResult recommendationResult){
        List<String> fleetNameList = new ArrayList<>();
        for (RecommendationFleet recommendationFleet:recommendationResult.getRecommendationFleetList()
             ) {
            fleetNameList.add(recommendationFleet.getFleet().getName());
        }
        return FleetRecommendationResponse.builder()
                .id(recommendationResult.getId())
                .fleetName(fleetNameList)
                .build();
    }

}
