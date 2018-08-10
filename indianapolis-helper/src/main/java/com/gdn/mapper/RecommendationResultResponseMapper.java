package com.gdn.mapper;

import com.gdn.entity.RecommendationFleet;
import com.gdn.entity.RecommendationResult;
import com.gdn.response.RecommendationResultFleetResponse;
import com.gdn.response.RecommendationResultResponse;

import java.util.ArrayList;
import java.util.List;

public class RecommendationResultResponseMapper {

    public static RecommendationResultResponse toRecommendationResultResponse(RecommendationResult recommendationResult){
        List<RecommendationResultFleetResponse> fleetResponseList = new ArrayList<>();
        for (RecommendationFleet recommendationFleet:recommendationResult.getRecommendationFleetList()
             ) {
            RecommendationResultFleetResponse fleet = RecommendationResultFleetResponse.builder()
                    .fleetId(recommendationFleet.getFleet().getId())
                    .fleetName(recommendationFleet.getFleet().getName())
                    .logisticVendorResponseList(
                            LogisticVendorResponseMapper.toLogisticVendorResponseList(
                                    LogisticVendorMapper.toLogisticVendorList(recommendationFleet.getFleet().getLogisticVendorFleetList())
                            ))
                    .build();
            fleetResponseList.add(fleet);
        }
        return RecommendationResultResponse.builder()
                .id(recommendationResult.getId())
                .fleetResponseList(fleetResponseList)
                .build();
    }

}
