package com.gdn.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PickupChoiceResponse {

    public Date pickupDate;

    public String fleetName;

    public String fleetPlateNumber;

}
