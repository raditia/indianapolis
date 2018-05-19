package com.gdn.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FleetRecommendationResponse {

    private String id;

    private List<String> fleetName;

}
