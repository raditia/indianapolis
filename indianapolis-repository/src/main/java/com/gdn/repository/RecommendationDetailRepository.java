package com.gdn.repository;

import com.gdn.entity.RecommendationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationDetailRepository extends JpaRepository<RecommendationDetail, String> {
}
