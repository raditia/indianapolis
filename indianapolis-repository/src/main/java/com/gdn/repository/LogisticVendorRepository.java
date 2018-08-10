package com.gdn.repository;

import com.gdn.entity.LogisticVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticVendorRepository extends JpaRepository<LogisticVendor, String> {
}
