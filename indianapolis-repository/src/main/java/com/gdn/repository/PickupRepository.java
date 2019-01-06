package com.gdn.repository;

import com.gdn.entity.Pickup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupRepository extends JpaRepository<Pickup, String> {
}
