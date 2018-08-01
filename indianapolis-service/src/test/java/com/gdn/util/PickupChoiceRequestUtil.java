package com.gdn.util;

import com.gdn.helper.DateHelper;
import com.gdn.request.PickupChoiceRequest;

public class PickupChoiceRequestUtil {
    public static PickupChoiceRequest pickupChoiceRequestCompleteAttribute = PickupChoiceRequest.builder()
            .recommendationResultId("id")
            .pickupDate(DateHelper.tomorrow())
            .build();
}
