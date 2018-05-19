package com.gdn.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecommendationResponse {

    private String warehouseName;

    private double cbmTotal;

    private List<FleetRecommendationResponse> fleetRecommendationResponseList;

}
