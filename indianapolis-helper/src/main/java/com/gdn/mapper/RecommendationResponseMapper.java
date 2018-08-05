package com.gdn.mapper;

import com.gdn.entity.RecommendationResult;
import com.gdn.response.FleetRecommendationResponse;
import com.gdn.response.RecommendationResponse;

import java.util.ArrayList;
import java.util.List;

public class RecommendationResponseMapper {

    public static RecommendationResponse toRecommendationResponse(List<RecommendationResult> recommendationResultList){
        String warehouse="";
        Float cbmTotal=0.0f;
        List<FleetRecommendationResponse> fleetRecommendationResponseList = new ArrayList<>();
        for (RecommendationResult recommendationResult:recommendationResultList
             ) {
            warehouse = recommendationResult.getWarehouse().getAddress();
            cbmTotal = recommendationResult.getTotalCbm();
            fleetRecommendationResponseList.add(FleetRecommendationResponseMapper.toFleetRecommendationResponse(recommendationResult));
        }
        return RecommendationResponse.builder()
                .warehouseName(warehouse)
                .cbmTotal(cbmTotal)
                .fleetRecommendationResponseList(fleetRecommendationResponseList)
                .build();
    }

}
