package com.gdn;

import com.gdn.response.RecommendationResultFleetResponse;

import java.util.ArrayList;
import java.util.List;

public class RecommendationResultFleetResponseUtil {
    public static RecommendationResultFleetResponse recommendationResultFleetResponse = RecommendationResultFleetResponse.builder()
            .fleetId(FleetUtil.fleetMotorCompleteAttribute.getId())
            .fleetName(FleetUtil.fleetMotorCompleteAttribute.getName())
            .build();
    public static List<RecommendationResultFleetResponse> recommendationResultFleetResponseList = new ArrayList<RecommendationResultFleetResponse>(){{
        add(recommendationResultFleetResponse);
    }};
}
