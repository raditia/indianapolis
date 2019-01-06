package com.gdn.repository;

import com.gdn.entity.Cff;
import com.gdn.entity.User;
import com.gdn.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CffRepository extends JpaRepository<Cff, String>{
    List<Cff> findAllByUploadedDateBetweenOrderByWarehouseAsc(Date startTime, Date endTime);
    @Query("SELECT DISTINCT c.warehouse from Cff c where c.schedulingStatus='PENDING'")
    List<Warehouse> findDistinctWarehouse();
}
