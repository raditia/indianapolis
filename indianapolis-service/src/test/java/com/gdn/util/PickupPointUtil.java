package com.gdn.util;

import com.gdn.entity.PickupPoint;

public class PickupPointUtil {
    public static PickupPoint pickupPointCompleteAttribute = PickupPoint.builder()
            .id("1")
            .pickupAddress("alamat pickup point 1")
            .latitude(0.0)
            .longitude(0.0)
            .allowedVehicleList(AllowedVehicleUtil.allowedVehicleListCompleteAttribute)
            .build();
    public static PickupPoint newPickupPointUploadCff = PickupPoint.builder()
            .pickupAddress("pickup point baru")
            .latitude(0.0)
            .longitude(0.0)
            .allowedVehicleList(AllowedVehicleUtil.allowedVehicleListUploadCff)
            .build();
    public static PickupPoint existingPickupPointUploadCff = PickupPoint.builder()
            .pickupAddress("alamat pickup point 1")
            .latitude(0.0)
            .longitude(0.0)
            .allowedVehicleList(AllowedVehicleUtil.allowedVehicleListUploadCff)
            .build();
}
