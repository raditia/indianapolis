package com.gdn.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class PickupChoiceResponse {

    public Date pickupDate;

    public List<PickupChoiceFleetResponse> fleetList;

}
