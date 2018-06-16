package com.gdn.repository;

import com.gdn.entity.Pickup;
import com.gdn.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickupRepository extends JpaRepository<Pickup, String> {
    List<Pickup> findAllByWarehouse(Warehouse warehouse);
}
