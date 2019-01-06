package com.gdn;

import com.gdn.response.RecommendationResultResponse;

import java.util.ArrayList;
import java.util.List;

public class RecommendationResultResponseUtil {
    public static RecommendationResultResponse recommendationResultResponseUtilCompleteAttribute = RecommendationResultResponse.builder()
            .id(RecommendationResultUtil.recommendationResultCompleteAttribute.getId())
            .fleetResponseList(RecommendationResultFleetResponseUtil.recommendationResultFleetResponseList)
            .build();
    public static List<RecommendationResultResponse> recommendationResultResponseListCompleteAttribute = new ArrayList<RecommendationResultResponse>(){{
        add(recommendationResultResponseUtilCompleteAttribute);
    }};
}
