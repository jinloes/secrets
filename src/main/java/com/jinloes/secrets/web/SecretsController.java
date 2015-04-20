package com.jinloes.secrets.web;

import com.jinloes.secrets.api.SecretRepository;
import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for interacting with {@link Secret} objects.
 */
@RestController
@ExposesResourceFor(Secret.class)
@RequestMapping("/secrets")
public class SecretsController {
    private final SecretRepository secretRepository;
    private final TextEncryptor encryptor;

    @Autowired
    public SecretsController(SecretRepository secretRepository, TextEncryptor encryptor) {
        this.secretRepository = secretRepository;
        this.encryptor = encryptor;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSecret(@RequestBody Secret secret, @AuthenticationPrincipal User user) {
        secret.setCreatedBy(user.getId());
        secret.setSecret(encryptor.encrypt(secret.getSecret()));
        secretRepository.save(secret);
    }
}
