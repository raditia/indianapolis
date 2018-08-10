package com.gdn;

import com.gdn.recommendation.DetailPickup;

import java.util.ArrayList;
import java.util.List;

public class DetailPickupUtil {
    public static DetailPickup detailPickup = DetailPickup.builder()
            .pickupAmount(10)
            .pickupCbm(10.0f)
            .product(ProductUtil.product)
            .build();
    public static DetailPickup detailPickup2 = DetailPickup.builder()
            .pickupAmount(10)
            .pickupCbm(10.0f)
            .product(ProductUtil.product)
            .build();
    public static DetailPickup detailPickup3 = DetailPickup.builder()
            .pickupAmount(2)
            .pickupCbm(0.4f)
            .product(ProductUtil.product)
            .build();
    public static Integer totalPickupAmount = 20;
    public static Float totalPickupCbm = 20.0f;
    public static List<DetailPickup> detailPickupList = new ArrayList<DetailPickup>(){{
        add(detailPickup);
        add(detailPickup2);
    }};
    public static List<DetailPickup> detailPickupList2 = new ArrayList<DetailPickup>(){{
        add(detailPickup3);
    }};
}
