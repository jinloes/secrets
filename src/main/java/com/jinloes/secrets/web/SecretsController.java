package com.jinloes.secrets.web;

import java.util.UUID;

import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;
import com.jinloes.secrets.resources.secret.SecretResource;
import com.jinloes.secrets.resources.secret.SecretResourceAssembler;
import com.jinloes.secrets.service.api.SecretService;
import com.jinloes.secrets.util.ResourceNotFoundException;
import com.jinloes.secrets.util.RestPreconditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final SecretService secretService;
    private final SecretResourceAssembler secretResourceAssembler;
    private final EntityLinks entityLinks;

    @Autowired
    public SecretsController(SecretService secretService,
            SecretResourceAssembler secretResourceAssembler, EntityLinks entityLinks) {
        this.secretService = secretService;
        this.secretResourceAssembler = secretResourceAssembler;
        this.entityLinks = entityLinks;
    }

    /**
     * Creates a secret
     *
     * @param secret secret request object
     * @param user   currently logged in user
     * @return created resource response
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedResourceResponse createSecret(@RequestBody Secret secret,
            @AuthenticationPrincipal User user) {
        Secret createdSecret = secretService.save(secret, user);
        return new CreatedResourceResponse(createdSecret.getId(), entityLinks, Secret.class);
    }

    /**
     * Gets a secret.
     *
     * @param secretId secret id
     * @return secret resource if exists,
     * otherwise {@link ResourceNotFoundException} will be thrown.
     */
    @RequestMapping(value = "/{secretId}", method = RequestMethod.GET)
    public SecretResource getSecret(@PathVariable("secretId") UUID secretId) {
        if (!secretService.exists(secretId)) {
            throw new ResourceNotFoundException();
        }
        Secret secret = secretService.getSecret(secretId);
        RestPreconditions.checkNotNull(secret);
        return secretResourceAssembler.toResource(secret);
    }

}
