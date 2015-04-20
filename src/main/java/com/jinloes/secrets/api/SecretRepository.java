package com.jinloes.secrets.api;

import java.util.UUID;

import com.jinloes.secrets.model.Secret;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for {@link Secret} objects.
 */
public interface SecretRepository extends JpaRepository<Secret, UUID> {
    Page<Secret> findByCreatedBy(UUID createdBy, Pageable pageable);
}
