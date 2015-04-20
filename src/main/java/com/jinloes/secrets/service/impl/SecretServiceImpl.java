package com.jinloes.secrets.service.impl;

import com.jinloes.secrets.api.SecretRepository;
import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;
import com.jinloes.secrets.service.api.SecretService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link SecretService}.
 */
@Service
public class SecretServiceImpl implements SecretService {
    private final SecretRepository secretRepository;

    @Autowired
    public SecretServiceImpl(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    @Override
    //@PostAuthorize("1 == 2")
    //@PreAuthorize("1 == 2")
    @PreAuthorize("hasPermission(#createdBy, 'read-secrets')")
    //@PreAuthorize("createdBy.getId() == principal.getId()")
    public Page<Secret> getByCreatedBy(User createdBy, Pageable pageable) {
        return secretRepository.findByCreatedBy(createdBy.getId(), pageable);
    }
}
