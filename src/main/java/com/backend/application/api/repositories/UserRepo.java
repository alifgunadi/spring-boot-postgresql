package com.backend.application.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.application.api.models.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
