package com.gdn.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecommendationResultResponse {

    private String id;

    private List<RecommendationResultFleetResponse> fleetResponseList;

}
