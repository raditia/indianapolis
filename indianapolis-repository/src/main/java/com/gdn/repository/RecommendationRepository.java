package com.gdn.repository;

import com.gdn.entity.CffGood;
import com.gdn.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RecommendationRepository extends JpaRepository<CffGood, String> {
    @Query("SELECT " +
            "count(cg.id) " +
            "from " +
            "CffGood cg, " +
            "AllowedVehicle av, " +
            "Cff c, " +
            "PickupPoint pp, " +
            "Fleet f," +
            "Merchant m " +
            "WHERE " +
            "cg.cff=c.id AND " +
            "av.pickupPoint=pp.id AND " +
            "c.pickupPoint=pp.id AND " +
            "av.vehicleName=f.name AND " +
            "c.warehouse=:warehouseId AND " +
            "c.pickupDate=:pickupDate AND " +
            "c.merchant=m.id AND " +
            "c.schedulingStatus='pending'")
    int getRowCount(@Param("warehouseId") Warehouse warehouse,
                    @Param("pickupDate") Date pickupDate);
}
