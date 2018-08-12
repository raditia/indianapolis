package com.gdn.repository;

import com.gdn.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickupDetailRepository extends JpaRepository<PickupDetail, String> {
    List<PickupDetail> findAllByPickupFleetPickupAndMerchant(Pickup pickup, Merchant merchant);
    List<PickupDetail> findAllByPickupFleetPickupAndCffGoodCffTpId(Pickup pickup, String tpId);
}
