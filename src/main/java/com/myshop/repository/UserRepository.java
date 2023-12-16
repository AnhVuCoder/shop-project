package com.myshop.repository;

import com.myshop.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Long countById(Integer id);
    @Query("UPDATE User u SET u.enabled=?2 WHERE u.id=?1")
    @Modifying
    void editStatusUser(Integer id, boolean enabled);
    @Query("SELECT u FROM User u WHERE CONCAT(u.id,' ',u.email,' ',u.firstName,' ',u.lastName) LIKE %?1%" )
    Page<User> findAll(String keyword, Pageable pageable);
}

