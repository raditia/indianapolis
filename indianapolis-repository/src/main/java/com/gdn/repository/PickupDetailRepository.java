package com.gdn.repository;

import com.gdn.entity.PickupDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupDetailRepository extends JpaRepository<PickupDetail, String> {
}
