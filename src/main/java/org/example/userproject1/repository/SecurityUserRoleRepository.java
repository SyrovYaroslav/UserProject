package org.example.userproject1.repository;

import org.example.userproject1.entity.SecurityUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityUserRoleRepository extends JpaRepository<SecurityUserRole, String> {

}
