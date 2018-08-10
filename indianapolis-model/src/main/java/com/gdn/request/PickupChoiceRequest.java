package com.gdn.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PickupChoiceRequest {

    public String recommendationResultId;

    public List<FleetChoiceRequest> fleetChoiceRequestList;

}
