package com.gdn.pickup;

import com.gdn.entity.Pickup;
import com.gdn.request.PickupChoiceRequest;

import java.util.List;

public interface PickupService {
    List<Pickup> savePickup(PickupChoiceRequest pickupChoiceRequest);
}
