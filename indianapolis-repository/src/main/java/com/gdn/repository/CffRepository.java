package com.gdn.repository;

import com.gdn.SchedulingStatus;
import com.gdn.entity.Cff;
import com.gdn.entity.User;
import com.gdn.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CffRepository extends JpaRepository<Cff, String>{
    List<Cff> findAllByOrderByWarehouseAsc();
    @Query("SELECT DISTINCT c.warehouse from Cff c where c.pickupDate=:pickupDate and c.schedulingStatus='PENDING'")
    List<Warehouse> findDistinctWarehouseAndPickupDate(@Param("pickupDate") Date pickupDate);
    List<Cff> findAllByTpAndPickupDateAndWarehouse(User tp, Date pickupDate, Warehouse warehouse);
}
