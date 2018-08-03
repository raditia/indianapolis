package com.gdn;

import com.gdn.entity.PickupDetail;

import java.util.ArrayList;
import java.util.List;

public class PickupDetailUtil {
    public static PickupDetail pickupDetailMinusPickupFleet = PickupDetail.builder()
            .id("1")
            .cffGood(RecommendationDetailUtil.recommendationDetailMinusRecommendationFleet.getCffGood())
            .cbmPickupAmount(0.0f)
            .merchant(RecommendationDetailUtil.recommendationDetailMinusRecommendationFleet.getMerchant())
            .pickupPoint(RecommendationDetailUtil.recommendationDetailMinusRecommendationFleet.getPickupPoint())
            .skuPickupQuantity(0)
            .build();
    public static List<PickupDetail> pickupDetailListMinusPickupFleet = new ArrayList<PickupDetail>(){{
        add(pickupDetailMinusPickupFleet);
    }};
}
