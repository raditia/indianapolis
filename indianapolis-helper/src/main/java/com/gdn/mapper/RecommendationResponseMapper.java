package com.gdn.mapper;

import com.gdn.entity.RecommendationResult;
import com.gdn.response.RecommendationResultResponse;
import com.gdn.response.RecommendationResponse;

import java.util.ArrayList;
import java.util.List;

public class RecommendationResponseMapper {

    public static RecommendationResponse toRecommendationResponse(List<RecommendationResult> recommendationResultList){
        String warehouse="";
        Float cbmTotal=0.0f;
        List<RecommendationResultResponse> recommendationResultResponseList = new ArrayList<>();
        for (RecommendationResult recommendationResult:recommendationResultList
             ) {
            warehouse = recommendationResult.getWarehouse().getAddress();
            cbmTotal = recommendationResult.getTotalCbm();
            recommendationResultResponseList.add(RecommendationResultResponseMapper.toRecommendationResultResponse(recommendationResult));
        }
        return RecommendationResponse.builder()
                .warehouseName(warehouse)
                .cbmTotal(cbmTotal)
                .recommendationResultResponseList(recommendationResultResponseList)
                .build();
    }

}
