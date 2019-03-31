package com.gdn.genetic_algorithm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneRecommendationResult {
    private double cbm;
    private String sku;
    private double length;
}
