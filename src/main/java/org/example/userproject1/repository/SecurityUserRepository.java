package org.example.userproject1.repository;

import org.example.userproject1.entity.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityUserRepository extends JpaRepository<SecurityUser, String> {
    SecurityUser findByUsername(String username);
}
