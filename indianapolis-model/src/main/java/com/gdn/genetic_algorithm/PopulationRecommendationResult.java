package com.gdn.genetic_algorithm;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PopulationRecommendationResult {
    private int index;
    private List<IndividualRecommendationResult> individualList;

}
