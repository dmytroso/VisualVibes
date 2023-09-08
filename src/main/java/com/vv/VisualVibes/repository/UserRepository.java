package com.vv.VisualVibes.repository;

import com.vv.VisualVibes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByName(String userName);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(Long id);
}