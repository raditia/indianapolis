package com.gdn.recommendation_algorithm;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.Pickup;
import com.gdn.recommendation.Product;

import java.util.List;

public interface PickupProcessorService {
    Pickup getNextPickup(List<Product> productList, Fleet topFleetWillUsed);
}
