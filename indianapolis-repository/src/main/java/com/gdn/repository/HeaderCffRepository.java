package com.gdn.repository;

import com.gdn.entity.HeaderCff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeaderCffRepository extends JpaRepository<HeaderCff, String> {
}
