package com.gdn.genetic_algorithm;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IndividualRecommendationResult {
    private String fleetName;
    private List<GeneRecommendationResult> geneList;
}
