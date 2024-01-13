package org.example.userproject1.repository;

import org.example.userproject1.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
