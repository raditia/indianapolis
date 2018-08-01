package com.gdn.pickup;

import com.gdn.entity.Pickup;
import com.gdn.request.PickupChoiceRequest;

public interface PickupService {
    Pickup savePickup(PickupChoiceRequest pickupChoiceRequest);
}
