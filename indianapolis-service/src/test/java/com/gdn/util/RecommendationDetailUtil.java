package com.gdn.util;

import com.gdn.entity.RecommendationDetail;

import java.util.ArrayList;
import java.util.List;

public class RecommendationDetailUtil {
    public static RecommendationDetail recommendationDetailMinusRecommendationFleet = RecommendationDetail.builder()
            .id("id")
            .merchant(MerchantUtil.merchantCompleteAttribute)
            .cffGood(CffGoodUtil.cffGoodMinusCff1)
            .pickupPoint(PickupPointUtil.pickupPointCompleteAttribute)
            .skuPickupQty(0)
            .cbmPickupAmount(0.0f)
            .build();
    public static List<RecommendationDetail> recommendationDetailListMinusRecommendationFleet = new ArrayList<RecommendationDetail>(){{
        add(recommendationDetailMinusRecommendationFleet);
    }};
}
