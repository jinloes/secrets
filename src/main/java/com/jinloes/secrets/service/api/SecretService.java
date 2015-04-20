package com.jinloes.secrets.service.api;

import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * A service for interacting with {@link Secret} objects.
 */
public interface SecretService {
    Page<Secret> getByCreatedBy(User createdBy, Pageable pageable);
}
