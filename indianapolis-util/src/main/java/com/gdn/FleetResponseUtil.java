package com.gdn;

import com.gdn.response.FleetResponse;

import java.util.ArrayList;
import java.util.List;

public class FleetResponseUtil {
    public static FleetResponse fleetResponseMotorCompleteAttribute = FleetResponse.builder()
            .name(FleetUtil.fleetMotorCompleteAttribute.getName())
            .build();
    public static FleetResponse fleetResponseVanCompleteAttribute = FleetResponse.builder()
            .name(FleetUtil.fleetVanCompleteAttribute.getName())
            .build();
    public static List<FleetResponse> descendingFleetResponseListCompleteAttribute = new ArrayList<FleetResponse>(){{
        add(fleetResponseVanCompleteAttribute);
        add(fleetResponseMotorCompleteAttribute);
    }};

}
