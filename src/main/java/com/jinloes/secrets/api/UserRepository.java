package com.jinloes.secrets.api;

import java.util.UUID;

import com.jinloes.secrets.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for {@link User} objects.
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
