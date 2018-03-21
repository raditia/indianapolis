package com.gdn.repository;

import com.gdn.entity.CffGood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CffGoodRepository extends JpaRepository<CffGood, Long> {
}
