package com.gdn.mapper;

import com.gdn.entity.PickupFleet;
import com.gdn.response.PickupChoiceFleetResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PickupChoiceFleetResponseMapper {
    public static PickupChoiceFleetResponse toPickupChoiceFleetResponse(PickupFleet pickupFleet){
        return PickupChoiceFleetResponse.builder()
                .fleetName(pickupFleet.getFleet().getName())
                .build();
    }
    public static List<PickupChoiceFleetResponse> toPickupChoiceFleetResponseList(List<PickupFleet> pickupFleetList){
        return pickupFleetList.stream()
                .map(PickupChoiceFleetResponseMapper::toPickupChoiceFleetResponse)
                .collect(Collectors.toList());
    }
}
