package com.exam.lifetext_test.repository;

import com.exam.lifetext_test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    public boolean existsByUsername(String username);
}
