package com.jinloes.secrets.api;

import java.util.UUID;

import com.jinloes.secrets.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A repository for interacting with users using JPA.
 */
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Finds a user by email;
     *
     * @param email
     * @return user
     */
    User findByEmail(String email);
}
