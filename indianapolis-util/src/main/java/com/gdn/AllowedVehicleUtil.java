package com.gdn;

import com.gdn.entity.AllowedVehicle;

import java.util.ArrayList;
import java.util.List;

public class AllowedVehicleUtil {
    public static AllowedVehicle allowedVehicleCompleteAttribute = AllowedVehicle.builder()
            .id("1")
            .vehicleName("nama vehicle 1")
            .pickupPoint(PickupPointUtil.pickupPointCompleteAttribute)
            .build();
    public static List<AllowedVehicle> allowedVehicleListCompleteAttribute = new ArrayList<AllowedVehicle>(){{
        add(allowedVehicleCompleteAttribute);
    }};

    public static AllowedVehicle allowedVehicleUploadCff = AllowedVehicle.builder()
            .vehicleName("nama vehicle 1")
            .build();
    public static List<AllowedVehicle> allowedVehicleListUploadCff = new ArrayList<AllowedVehicle>(){{
        add(allowedVehicleUploadCff);
    }};
}
