package com.gdn.recommendation;

import com.gdn.entity.Fleet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationFleetResult {
    private String recommendationId;
    private List<Fleet> fleetList;
}
