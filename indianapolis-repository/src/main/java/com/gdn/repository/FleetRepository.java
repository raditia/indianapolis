package com.gdn.repository;

import com.gdn.entity.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FleetRepository extends JpaRepository<Fleet, String> {
    List<Fleet> findAllByOrderByCbmCapacityDesc();

    @Query("SELECT DISTINCT f.name, f.cbmCapacity, f.id, f.price, f.minCbm, f.logisticVendor FROM Fleet f order by f.cbmCapacity DESC")
    List<Fleet> findDistinctByNameOrderByCbmCapacityDesc();
}
