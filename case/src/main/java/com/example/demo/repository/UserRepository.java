package com.example.demo.repository;

import com.example.demo.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String username);
    @Query("SELECT u FROM user u WHERE u.username = :value OR u.email = :value")
    Optional<User> findByUsernameOrEmail(@Param("value") String value);

    Optional<User> findUserById(Long id);
}
