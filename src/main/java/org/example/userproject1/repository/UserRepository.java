package org.example.userproject1.repository;

import org.example.userproject1.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.mail LIKE %:keyword%")
    Page<User> findUsersByQuery(@Param("keyword") String query, PageRequest pageRequest);
}
