package com.jinloes.secrets.service.api;

import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * A service for interacting with {@link Secret} objects.
 */
public interface SecretService {
    /**
     * Checks if a secret exists for the id.
     *
     * @param secretId secret id
     * @return true if the secret exists, otherwise false
     */
    boolean exists(String secretId);

    /**
     * Saves a secret
     *
     * @param secret  secret to save
     * @param creator secret creator
     * @return created secret
     */
    Secret save(Secret secret, User creator);

    /**
     * Gets a secret by its id.
     *
     * @param secretId secret id
     * @return secret
     */
    Secret getSecret(String secretId);

    /**
     * Gets secrets created by a user.
     *
     * @param createdBy user to get the secrets for
     * @param pageable  pageable object
     * @return secret page
     */
    Page<Secret> getByCreatedBy(User createdBy, Pageable pageable);

    /**
     * Updates a secret.
     *
     * @param secretUpdate secret update object
     * @param secret       secret
     * @param user         currently logged in user
     */
    void update(Secret secretUpdate, Secret secret, User user);

    /**
     * Deletes a secret.
     *
     * @param secret secret
     */
    void delete(Secret secret, User user);
}
