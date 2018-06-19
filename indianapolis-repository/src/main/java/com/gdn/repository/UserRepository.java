package com.gdn.repository;

import com.gdn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmailAddressAndPassword(String emailAddress, String password);
}
