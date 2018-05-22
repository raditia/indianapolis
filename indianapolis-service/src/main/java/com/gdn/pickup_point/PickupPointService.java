package com.gdn.pickup_point;

import com.gdn.entity.PickupPoint;

public interface PickupPointService {
    PickupPoint findByPickupAddressOrLatitudeOrLongitude(String address, double latitude, double longitude);
}
