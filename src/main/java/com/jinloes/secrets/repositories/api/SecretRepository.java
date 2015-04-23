package com.jinloes.secrets.repositories.api;

import com.jinloes.secrets.model.Secret;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * JPA repository for {@link Secret} objects.
 */
public interface SecretRepository extends MongoRepository<Secret, String> {
    Page<Secret> findByCreatedBy(String createdBy, Pageable pageable);
}
