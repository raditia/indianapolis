package com.gdn;

import com.gdn.entity.Pickup;

import java.util.ArrayList;
import java.util.List;

public class PickupUtil {
    public static Pickup pickupCompleteAttribute = Pickup.builder()
                .id("1")
                .pickupDate(RecommendationResultUtil.recommendationResultCompleteAttribute.getPickupDate())
                .warehouse(RecommendationResultUtil.recommendationResultCompleteAttribute.getWarehouse())
                .pickupFleetList(PickupFleetUtil.pickupFleetListMinusPickup)
                .build();
    public static com.gdn.recommendation.Pickup pickup = com.gdn.recommendation.Pickup.builder()
            .fleetIdNumber(FleetUtil.fleetMotorCompleteAttribute.getId())
            .fleet(FleetUtil.fleetMotorCompleteAttribute)
            .pickupTotalCbm(0.4f)
            .pickupTotalAmount(2)
            .detailPickupList(DetailPickupUtil.detailPickupList2)
            .build();
    public static com.gdn.recommendation.Pickup pickup1 = com.gdn.recommendation.Pickup.builder()
            .fleetIdNumber(FleetUtil.fleetMotorCompleteAttribute.getId())
            .fleet(FleetUtil.fleetMotorCompleteAttribute)
            .pickupTotalCbm(0.2f)
            .pickupTotalAmount(2)
            .detailPickupList(DetailPickupUtil.detailPickupList3)
            .build();
    public static com.gdn.recommendation.Pickup pickup2 = com.gdn.recommendation.Pickup.builder()
            .fleetIdNumber(FleetUtil.fleetMotorCompleteAttribute.getId())
            .fleet(FleetUtil.fleetMotorCompleteAttribute)
            .pickupTotalCbm(0.0f)
            .pickupTotalAmount(0)
            .detailPickupList(DetailPickupUtil.detailPickupList2)
            .build();
    public static List<com.gdn.recommendation.Pickup> pickupList = new ArrayList<com.gdn.recommendation.Pickup>(){{
        add(pickup);
    }};
    public static List<com.gdn.recommendation.Pickup> pickupList1 = new ArrayList<com.gdn.recommendation.Pickup>(){{
        add(pickup1);
    }};
}
