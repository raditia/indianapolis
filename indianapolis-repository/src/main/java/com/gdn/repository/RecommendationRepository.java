package com.gdn.repository;

import com.gdn.entity.CffGood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<CffGood, String> {
    @Query("SELECT COUNT(cg.id) as row_count from CffGood cg, AllowedVehicle av, Cff c, HeaderCff hc, PickupPoint pp, Fleet f WHERE cg.cff=c.id AND c.headerCff=hc.id AND pp.headerCff=hc.id AND av.pickupPoint=pp.id AND av.vehicleName=f.name")
    int getRowCount();
}
