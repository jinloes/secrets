package com.jinloes.secrets.web;

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
public class SecretController {
    private final SecretService secretService;
    private final SecretResourceAssembler secretResourceAssembler;
    private final EntityLinks entityLinks;

    @Autowired
    public SecretController(SecretService secretService,
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
    public ModifiedResourceResponse createSecret(@RequestBody Secret secret,
            @AuthenticationPrincipal User user) {
        Secret createdSecret = secretService.save(secret, user);
        return new ModifiedResourceResponse(createdSecret.getId(), entityLinks, Secret.class);
    }

    /**
     * Gets a secret.
     *
     * @param secretId secret id
     * @return secret resource if exists,
     * otherwise {@link ResourceNotFoundException} will be thrown.
     */
    @RequestMapping(value = "/{secretId}", method = RequestMethod.GET)
    public SecretResource getSecret(@PathVariable("secretId") String secretId) {
        Secret secret = secretService.getSecret(secretId);
        RestPreconditions.checkNotNull(secret);
        return secretResourceAssembler.toResource(secret);
    }

    /**
     * Deletes a secret.
     *
     * @param secretId     secret id
     * @param secretUpdate secret update request
     * @param user         currently logged in user
     * @return modified resource response
     */
    @RequestMapping(value = "/{secretId}", method = RequestMethod.PUT)
    public ModifiedResourceResponse updateSecret(@PathVariable("secretId") String secretId,
            @RequestBody Secret secretUpdate, @AuthenticationPrincipal User user) {
        Secret secret = secretService.getSecret(secretId);
        RestPreconditions.checkNotNull(secret);
        secretService.update(secretUpdate, secret, user);
        return new ModifiedResourceResponse(secretId, entityLinks, Secret.class);
    }

    /**
     * Deletes a secret.
     *
     * @param secretId secret id
     * @param user     currently logged in user.
     */
    @RequestMapping(value = "/{secretId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSecret(@PathVariable("secretId") String secretId,
            @AuthenticationPrincipal User user) {
        Secret secret = secretService.getSecret(secretId);
        RestPreconditions.checkNotNull(secret);
        secretService.delete(secret, user);
    }
}
