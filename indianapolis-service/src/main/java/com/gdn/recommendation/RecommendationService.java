package com.gdn.recommendation;

import java.util.List;

public interface RecommendationService {
    List<DatabaseQueryResult> executeBatch();
    List<DatabaseQueryResult> setPickupList(List<DatabaseQueryResult> pickupList);
    int getResultRowCount();
}
