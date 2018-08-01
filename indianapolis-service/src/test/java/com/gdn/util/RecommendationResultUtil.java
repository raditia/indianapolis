package com.gdn.util;

import com.gdn.entity.RecommendationResult;
import com.gdn.helper.DateHelper;

import java.util.ArrayList;
import java.util.List;

public class RecommendationResultUtil {
    public static RecommendationResult recommendationResultCompleteAttribute = RecommendationResult.builder()
            .id("id")
            .warehouse(WarehouseUtil.warehouseMinusWarehouseCategoryList)
            .pickupDate(DateHelper.tomorrow())
            .totalCbm(0.0f)
            .totalSku(0)
            .recommendationFleetList(RecommendationFleetUtil.recommendationFleetListMinusRecommendationResult)
            .build();
    public static List<RecommendationResult> recommendationResultListCompleteAttribute = new ArrayList<RecommendationResult>(){{
        add(recommendationResultCompleteAttribute);
    }};
}
