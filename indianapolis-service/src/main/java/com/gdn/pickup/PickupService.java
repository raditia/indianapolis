package com.gdn.pickup;

import com.gdn.request.PickupChoiceRequest;
import com.gdn.response.PickupChoiceResponse;
import com.gdn.response.WebResponse;

public interface PickupService {
    WebResponse<PickupChoiceResponse> savePickup(PickupChoiceRequest pickupChoiceRequest);
}
