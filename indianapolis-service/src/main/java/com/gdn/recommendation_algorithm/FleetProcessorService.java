package com.gdn.recommendation_algorithm;

import com.gdn.entity.Fleet;
import com.gdn.recommendation.DatabaseQueryResult;
import com.gdn.recommendation.Product;

import java.util.List;

public interface FleetProcessorService {
    List<Fleet> getTopThreeFleetsWillUsed(List<DatabaseQueryResult> resultList);
    Fleet getNextFleetWillUsed(List<Product> productList, Fleet maxFleet);
    Fleet getFleetWithMoreCbmCapacity(Fleet maxFleet);
}
