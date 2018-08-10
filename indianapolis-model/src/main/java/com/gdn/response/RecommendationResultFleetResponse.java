package com.gdn.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResultFleetResponse {
    private String recommendationFleetId;
    private String fleetId;
    private String fleetName;
    private List<LogisticVendorResponse> logisticVendorResponseList;
}
