package com.gdn.repository;

import com.gdn.entity.PickupPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupPointRepository extends JpaRepository<PickupPoint, String> {
    PickupPoint findByPickupAddressAndLatitudeAndLongitude(String pickupAddress, Double latitude, Double longitude);
}
