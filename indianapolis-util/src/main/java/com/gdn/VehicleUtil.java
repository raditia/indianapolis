package com.gdn;

import com.gdn.recommendation.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleUtil {
    public static Vehicle vehicle = Vehicle.builder()
            .name(FleetUtil.fleetMotorCompleteAttribute.getName())
            .cbmCapacity(FleetUtil.fleetMotorCompleteAttribute.getCbmCapacity())
            .minCbm(FleetUtil.fleetMotorCompleteAttribute.getMinCbm())
            .build();
    public static List<Vehicle> vehicleList = new ArrayList<Vehicle>(){{
        add(vehicle);
    }};
}
