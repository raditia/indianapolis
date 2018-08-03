package com.gdn;

import com.gdn.entity.RecommendationResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecommendationResultUtil {
    private static Date theDayAfterTomorrow(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    public static RecommendationResult recommendationResultCompleteAttribute = RecommendationResult.builder()
            .id("id")
            .warehouse(WarehouseUtil.warehouseMinusWarehouseCategoryList)
            .pickupDate(theDayAfterTomorrow())
            .totalCbm(0.0f)
            .totalSku(0)
            .recommendationFleetList(RecommendationFleetUtil.recommendationFleetListMinusRecommendationResult)
            .build();
    public static List<RecommendationResult> recommendationResultListCompleteAttribute = new ArrayList<RecommendationResult>(){{
        add(recommendationResultCompleteAttribute);
    }};
}
