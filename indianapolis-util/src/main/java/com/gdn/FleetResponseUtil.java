package com.gdn;

import com.gdn.response.FleetResponse;

import java.util.ArrayList;
import java.util.List;

public class FleetResponseUtil {
    public static FleetResponse fleetResponseCompleteAttribute = FleetResponse.builder()
            .name(FleetUtil.fleetMotorCompleteAttribute.getName())
            .build();
    public static List<FleetResponse> fleetResponseListCompleteAttribute = new ArrayList<FleetResponse>(){{
        add(fleetResponseCompleteAttribute);
    }};
}
