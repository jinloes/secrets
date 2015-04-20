package com.jinloes.secrets.web;

import com.jinloes.secrets.api.SecretRepository;
import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jinloes on 4/19/15.
 */
@RestController
@ExposesResourceFor(Secret.class)
@RequestMapping("/secrets")
public class SecretsController {
    private final SecretRepository secretRepository;

    @Autowired
    public SecretsController(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSecret(@RequestBody Secret secret, @AuthenticationPrincipal User user) {
        secret.setCreatedBy(user.getId());
        secretRepository.save(secret);
    }
}
