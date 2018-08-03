package com.gdn;

import com.gdn.entity.Fleet;

import java.util.ArrayList;
import java.util.List;

public class FleetUtil {
    public static Fleet fleetMotorCompleteAttribute = Fleet.builder()
            .id("1")
            .name("motor")
            .cbmCapacity(0.5f)
            .logisticVendor(LogisticVendorUtil.logisticVendorCompleteAttribute)
            .minCbm(0f)
            .price(0.0)
            .build();
    public static Fleet fleetVanCompleteAttribute = Fleet.builder()
            .id("2")
            .name("van")
            .cbmCapacity(1.5f)
            .minCbm(0.51f)
            .price(0.0)
            .build();
    public static List<Fleet> descendingFleetListCompleteAttribute = new ArrayList<Fleet>(){{
        add(fleetVanCompleteAttribute);
        add(fleetMotorCompleteAttribute);
    }};
    public static List<Fleet> ascendingFleetListCompleteAttribute = new ArrayList<Fleet>(){{
        add(fleetMotorCompleteAttribute);
        add(fleetVanCompleteAttribute);
    }};
}
