package com.gdn.pickup_point.implementation;

import com.gdn.entity.PickupPoint;
import com.gdn.pickup_point.PickupPointService;
import com.gdn.repository.PickupPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PickupPointServiceImpl implements PickupPointService {

    @Autowired
    private PickupPointRepository pickupPointRepository;

    @Override
    public PickupPoint findByPickupAddressOrLatitudeOrLongitude(String address, double latitude, double longitude) {
        return pickupPointRepository.findByPickupAddressOrLatitudeOrLongitude(address, latitude, longitude);
    }
}
