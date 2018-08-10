package com.gdn;

import com.gdn.request.FleetChoiceRequest;

import java.util.ArrayList;
import java.util.List;

public class FleetChoiceRequestUtil {
    public static FleetChoiceRequest fleetChoiceRequestMotorBes = FleetChoiceRequest.builder()
            .fleetId(FleetUtil.fleetMotorCompleteAttribute.getId())
            .logisticVendorId(LogisticVendorUtil.logisticVendorCompleteAttribute.getId())
            .build();
    public static List<FleetChoiceRequest> fleetChoiceRequestList = new ArrayList<FleetChoiceRequest>(){{
        add(fleetChoiceRequestMotorBes);
    }};
}
