package com.gdn;

import com.gdn.entity.PickupFleet;

import java.util.ArrayList;
import java.util.List;

public class PickupFleetUtil {
    public static PickupFleet pickupFleetMinusPickup = PickupFleet.builder()
            .id("1")
            .fleet(RecommendationFleetUtil.recommendationFleetMinusRecommendationResult.getFleet())
            .logisticVendor(LogisticVendorUtil.logisticVendorCompleteAttribute)
            .plateNumber("1")
            .pickupDetailList(PickupDetailUtil.pickupDetailListMinusPickupFleet)
            .build();
    public static List<PickupFleet> pickupFleetListMinusPickup = new ArrayList<PickupFleet>(){{
        add(pickupFleetMinusPickup);
    }};
}
