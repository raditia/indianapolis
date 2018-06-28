package com.gdn.recommendation_algorithm;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.Sku;

import java.util.List;

public interface FleetProcessorService {
    List<Fleet> getMaxThree(List<Sku> skuList);
    Fleet getNext(List<Sku> skuList, Fleet maxFleet);
    Fleet getUp(Fleet maxFleet);
}
