package com.gdn.util;

import com.gdn.response.PickupChoiceFleetResponse;

import java.util.ArrayList;
import java.util.List;

public class PickupChoiceFleetResponseUtil {
    public static PickupChoiceFleetResponse pickupChoiceFleetResponse = PickupChoiceFleetResponse.builder()
            .fleetName(PickupFleetUtil.pickupFleetMinusPickup.getFleet().getName())
            .build();
    public static List<PickupChoiceFleetResponse> pickupChoiceFleetResponseList = new ArrayList<PickupChoiceFleetResponse>(){{
        add(pickupChoiceFleetResponse);
    }};
}
