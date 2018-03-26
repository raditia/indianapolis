package com.gdn.repository;

import com.gdn.entity.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FleetRepository extends JpaRepository<Fleet, String> {
    List<Fleet> findAllByOrderByCbmCapacityDesc();
}
