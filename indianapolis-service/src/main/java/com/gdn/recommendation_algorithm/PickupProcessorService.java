package com.gdn.recommendation_algorithm;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.Sku;

import java.util.List;

public interface PickupProcessorService {
    Pickup getPickup(List<Sku> skuList, Fleet maxFleet);
}
