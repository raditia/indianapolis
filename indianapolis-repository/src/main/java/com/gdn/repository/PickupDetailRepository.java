package com.gdn.repository;

import com.gdn.entity.Merchant;
import com.gdn.entity.Pickup;
import com.gdn.entity.PickupDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickupDetailRepository extends JpaRepository<PickupDetail, String> {
    List<PickupDetail> findAllByPickupAndMerchant(Pickup pickup, Merchant merchant);
}
