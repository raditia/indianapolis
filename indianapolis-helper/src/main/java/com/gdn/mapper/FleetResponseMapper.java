package com.gdn.mapper;

import com.gdn.entity.Fleet;
import com.gdn.response.FleetResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FleetResponseMapper {

    public static FleetResponse toFleetResponse(Fleet fleet){
        return FleetResponse.builder()
                .name(fleet.getName())
                .build();
    }

    public static List<FleetResponse> toFleetResponseList(List<Fleet> fleetList){
        return fleetList.stream()
                .map(FleetResponseMapper::toFleetResponse)
                .collect(Collectors.toList());
    }

}
