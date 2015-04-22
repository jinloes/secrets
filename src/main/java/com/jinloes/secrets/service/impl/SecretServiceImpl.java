package com.jinloes.secrets.service.impl;

import java.util.UUID;

import com.jinloes.secrets.api.SecretRepository;
import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;
import com.jinloes.secrets.service.api.SecretService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link SecretService}.
 */
@Service
public class SecretServiceImpl implements SecretService {
    private final SecretRepository secretRepository;
    private final TextEncryptor encryptor;

    @Autowired
    public SecretServiceImpl(SecretRepository secretRepository, TextEncryptor encryptor) {
        this.secretRepository = secretRepository;
        this.encryptor = encryptor;
    }

    @Override
    public boolean exists(UUID secretId) {
        return secretRepository.exists(secretId);
    }

    @Override
    public Secret save(Secret secret, User creator) {
        secret.setCreatedBy(creator.getId());
        // Encrypt the secret
        secret.setSecret(encryptor.encrypt(secret.getSecret()));
        return secretRepository.save(secret);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'read')")
    public Secret getSecret(UUID secretId) {
        return secretRepository.getOne(secretId);
    }

    @Override
    @PreAuthorize("hasPermission(#createdBy, 'read-secrets')")
    public Page<Secret> getByCreatedBy(User createdBy, Pageable pageable) {
        return secretRepository.findByCreatedBy(createdBy.getId(), pageable);
    }
}
