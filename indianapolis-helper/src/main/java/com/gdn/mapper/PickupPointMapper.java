package com.gdn.mapper;

import com.gdn.entity.PickupPoint;
import com.gdn.request.PickupPointRequest;

public class PickupPointMapper {
    public static PickupPoint toPickupPoint(PickupPointRequest pickupPointRequest){
        return PickupPoint.builder()
                .pickupAddress(pickupPointRequest.getPickupAddress())
                .latitude(pickupPointRequest.getLatitude())
                .longitude(pickupPointRequest.getLongitude())
                .allowedVehicleList(AllowedVehicleMapper.toAllowedVehicleList(pickupPointRequest.getAllowedVehicleList()))
                .build();
    }
}
