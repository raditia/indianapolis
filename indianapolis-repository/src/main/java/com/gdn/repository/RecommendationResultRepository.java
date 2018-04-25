package com.gdn.repository;

import com.gdn.entity.RecommendationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationResultRepository extends JpaRepository<RecommendationResult, String> {
}
