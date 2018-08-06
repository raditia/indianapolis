package com.gdn;

import com.gdn.request.PickupPointRequest;

public class PickupPointRequestUtil {
    public static PickupPointRequest existingPickupPointRequestCompleteAttribute = PickupPointRequest.builder()
            .pickupAddress(PickupPointUtil.pickupPointCompleteAttribute.getPickupAddress())
            .latitude(PickupPointUtil.pickupPointCompleteAttribute.getLatitude())
            .longitude(PickupPointUtil.pickupPointCompleteAttribute.getLongitude())
            .allowedVehicleList(AllowedVehicleRequestUtil.allowedVehicleRequestListCompleteAttribute)
            .build();
    public static PickupPointRequest newPickupPointCompleteAttribute = PickupPointRequest.builder()
            .pickupAddress("new pickup address")
            .latitude(0.0)
            .longitude(0.0)
            .allowedVehicleList(AllowedVehicleRequestUtil.allowedVehicleRequestListCompleteAttribute)
            .build();
}
