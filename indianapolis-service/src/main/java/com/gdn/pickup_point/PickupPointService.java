package com.gdn.pickup_point;

import com.gdn.entity.PickupPoint;

public interface PickupPointService {
    PickupPoint findByPickupAddressOrLatitudeAndLongitude(String pickupAddress, Double latitude, Double longitude);
}
