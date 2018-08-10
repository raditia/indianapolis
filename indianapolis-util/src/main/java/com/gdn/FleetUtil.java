package com.gdn;

import com.gdn.entity.Fleet;

import java.util.ArrayList;
import java.util.List;

public class FleetUtil {
    public static Fleet fleetMotorCompleteAttribute = Fleet.builder()
            .id("1")
            .name("motor")
            .cbmCapacity(0.5f)
            .minCbm(0.0f)
            .price(0.0)
            .logisticVendorFleetList(LogisticVendorFleetUtil.logisticVendorFleetListLogisticVendorOnly)
            .build();
    public static Fleet fleetVanCompleteAttribute = Fleet.builder()
            .id("2")
            .name("van")
            .cbmCapacity(1.5f)
            .minCbm(0.51f)
            .price(0.0)
            .logisticVendorFleetList(LogisticVendorFleetUtil.logisticVendorFleetListLogisticVendorOnly)
            .build();
    private static Fleet fleetDouble = Fleet.builder()
            .id("3")
            .name("double")
            .cbmCapacity(10.0f)
            .minCbm(5.0f)
            .price(0.0)
            .build();
    private static Fleet fleetEngkel = Fleet.builder()
            .id("4")
            .name("engkel")
            .cbmCapacity(4.0f)
            .minCbm(2.0f)
            .price(0.0)
            .build();
    public static List<Fleet> fleetListMotorOnly = new ArrayList<Fleet>(){{
        add(fleetMotorCompleteAttribute);
    }};
    public static List<Fleet> descendingFleetListCompleteAttribute = new ArrayList<Fleet>(){{
        add(fleetVanCompleteAttribute);
        add(fleetMotorCompleteAttribute);
    }};
    public static List<Fleet> ascendingFleetListCompleteAttribute = new ArrayList<Fleet>(){{
        add(fleetMotorCompleteAttribute);
        add(fleetVanCompleteAttribute);
    }};
    public static List<Fleet> topThreeFleetsWillUsed = new ArrayList<Fleet>(){{
        add(fleetVanCompleteAttribute);
        add(fleetEngkel);
        add(fleetDouble);
    }};
    public static List<Fleet> topFleetsWillUsed = new ArrayList<Fleet>(){{
        add(fleetMotorCompleteAttribute);
    }};
    public static Float restCbmCanBeUsed = fleetMotorCompleteAttribute.getCbmCapacity();
    public static Float restCbmCanBeUsedAfterUpdate = 0.1f;
}
