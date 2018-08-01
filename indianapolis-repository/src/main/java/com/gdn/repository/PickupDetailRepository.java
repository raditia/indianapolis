package com.gdn.repository;

import com.gdn.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickupDetailRepository extends JpaRepository<PickupDetail, String> {
    List<PickupDetail> findAllByPickupFleetAndMerchant(PickupFleet pickupFleet, Merchant merchant);
    List<PickupDetail> findAllByPickupFleetAndCffGoodCff(PickupFleet pickupFleet, Cff cff);
}
