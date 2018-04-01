package com.gdn.repository;

import com.gdn.entity.AllowedVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowedVehicleRepository extends JpaRepository<AllowedVehicle, String> {
}
