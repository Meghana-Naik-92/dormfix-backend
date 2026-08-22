package com.dormfix.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dormfix.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByResetToken(String resetToken);
}


/*
writing an interface with no method bodies, yet it works. Spring Data JPA reads the method names (findByEmail, existsByEmail) and generates the SQL automatically at runtime. findByEmail becomes SELECT * FROM users WHERE email = ? without you writing a single line of SQL.
 */
