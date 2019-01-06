package com.gdn.mapper;

import com.gdn.entity.Pickup;
import com.gdn.response.PickupChoiceResponse;

public class PickupChoiceResponseMapper {

    public static PickupChoiceResponse toPickupChoiceResponse(Pickup pickup){
        return PickupChoiceResponse.builder()
                .pickupDate(pickup.getPickupDate())
                .fleetList(PickupChoiceFleetResponseMapper.toPickupChoiceFleetResponseList(pickup.getPickupFleetList()))
                .build();
    }

}
