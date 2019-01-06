package com.gdn;

import com.gdn.recommendation.DetailPickup;

import java.util.ArrayList;
import java.util.List;

public class DetailPickupUtil {
    public static DetailPickup detailPickup3 = DetailPickup.builder()
            .pickupAmount(2)
            .pickupCbm(0.2f)
            .product(ProductUtil.product2)
            .build();
    public static DetailPickup detailPickup2 = DetailPickup.builder()
            .pickupAmount(2)
            .pickupCbm(0.4f)
            .product(ProductUtil.product)
            .build();

    public static List<DetailPickup> detailPickupList2 = new ArrayList<DetailPickup>(){{
        add(detailPickup2);
    }};
    public static List<DetailPickup> detailPickupList3 = new ArrayList<DetailPickup>(){{
        add(detailPickup3);
    }};
}
