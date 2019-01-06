package com.gdn.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PickupChoiceFleetResponse {

    private String fleetName;

    private String logisticVendorName;

}
