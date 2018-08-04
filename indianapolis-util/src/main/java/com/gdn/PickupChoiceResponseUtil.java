package com.gdn;

import com.gdn.response.PickupChoiceResponse;

public class PickupChoiceResponseUtil {
    public static PickupChoiceResponse pickupChoiceResponseCompleteAttribute = PickupChoiceResponse.builder()
            .pickupDate(PickupUtil.pickupCompleteAttribute.getPickupDate())
            .fleetList(PickupChoiceFleetResponseUtil.pickupChoiceFleetResponseList)
            .build();
}
