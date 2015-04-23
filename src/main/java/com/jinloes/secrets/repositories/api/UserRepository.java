package com.jinloes.secrets.repositories.api;

import com.jinloes.secrets.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Mongo repository for {@link User} objects.
 */
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Finds a user by email;
     *
     * @param email
     * @return user
     */
    User findByEmail(String email);
}
