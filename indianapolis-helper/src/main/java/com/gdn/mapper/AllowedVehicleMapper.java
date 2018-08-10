package com.gdn.mapper;

import com.gdn.entity.AllowedVehicle;
import com.gdn.request.AllowedVehicleRequest;

import java.util.List;
import java.util.stream.Collectors;

public class AllowedVehicleMapper {
    public static AllowedVehicle toAllowedVehicle(AllowedVehicleRequest allowedVehicleRequest){
        return AllowedVehicle.builder()
                .vehicleName(allowedVehicleRequest.getVehicleName())
                .build();
    }
    public static List<AllowedVehicle> toAllowedVehicleList(List<AllowedVehicleRequest> allowedVehicleRequestList){
        return allowedVehicleRequestList.stream()
                .map(AllowedVehicleMapper::toAllowedVehicle)
                .collect(Collectors.toList());
    }
}
