package com.gdn;

import com.gdn.entity.Pickup;

public class PickupUtil {
    public static Pickup pickupCompleteAttribute = Pickup.builder()
                .id("1")
                .pickupDate(RecommendationResultUtil.recommendationResultCompleteAttribute.getPickupDate())
                .warehouse(RecommendationResultUtil.recommendationResultCompleteAttribute.getWarehouse())
                .pickupFleetList(PickupFleetUtil.pickupFleetListMinusPickup)
                .build();
}
