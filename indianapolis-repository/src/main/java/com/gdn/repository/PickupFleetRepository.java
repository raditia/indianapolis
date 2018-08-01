package com.gdn.repository;

import com.gdn.entity.LogisticVendor;
import com.gdn.entity.PickupFleet;
import com.gdn.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickupFleetRepository extends JpaRepository<PickupFleet, String> {
    List<PickupFleet> findAllByPickupWarehouse(Warehouse warehouse);
    List<PickupFleet> findAllByPickupWarehouseAndFleetLogisticVendor(Warehouse warehouse, LogisticVendor logisticVendor);
}
