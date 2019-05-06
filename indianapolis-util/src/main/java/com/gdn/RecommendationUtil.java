package com.gdn;

import com.gdn.recommendation.Recommendation;

import java.util.ArrayList;
import java.util.List;

public class RecommendationUtil {

    public static Recommendation recommendation = Recommendation.builder()
            .id("rec1")
//            .pickupList(PickupUtil.pickupList)
            .productAmount(2)
            .cbmTotal(0.0f)
            .warehouseId(WarehouseUtil.warehouseMinusWarehouseCategoryList.getId())
            .build();

    public static List<Recommendation> recommendationList = new ArrayList<Recommendation>() {{
       add(recommendation);
    }};
}
