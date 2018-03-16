package com.gdn.repository;

import com.gdn.entity.WarehouseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseCategoryRepository extends JpaRepository<WarehouseCategory, String> {
}
