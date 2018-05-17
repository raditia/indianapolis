package com.gdn.repository;

import com.gdn.entity.CffGood;
import com.gdn.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<CffGood, String> {
    @Query("SELECT COUNT (cg.id) from CffGood cg")
    int getRowCount();
}
