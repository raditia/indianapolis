package com.gdn.repository;

import com.gdn.entity.RecommendationDetail;
import com.gdn.entity.RecommendationFleet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationDetailRepository extends JpaRepository<RecommendationDetail, String> {
    List<RecommendationDetail> findAllByRecommendationFleetId(String recommendationFleetId);
}
