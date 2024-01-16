package org.example.userproject1.repository;

import org.example.userproject1.entity.Phone;
import org.example.userproject1.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    @Query("SELECT p FROM Phone p WHERE p.user.id = :id")
    Page<Phone> findAllPhoneByUserId(@Param("id") long id, PageRequest pageRequest);
    @Query("SELECT p FROM Phone p WHERE p.phoneNumber LIKE %:keyword% AND p.user.id = :id")
    Page<Phone> findPhoneByQuery(@Param("keyword") String query,@Param("id") long id, PageRequest pageRequest);
}
