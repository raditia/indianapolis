package com.gdn.repository;

import com.gdn.entity.RecommendationResult;
import com.gdn.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationResultRepository extends JpaRepository<RecommendationResult, String> {
    List<RecommendationResult> findByWarehouse(Warehouse warehouse);
    void deleteAllByWarehouse(Warehouse warehouse);
    @Query("SELECT DISTINCT rr.warehouse FROM RecommendationResult rr")
    List<Warehouse> findDistinctAllWarehouse();
}
