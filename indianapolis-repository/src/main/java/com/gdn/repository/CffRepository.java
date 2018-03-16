package com.gdn.repository;

import com.gdn.entity.Cff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CffRepository extends JpaRepository<Cff, String>{
}
