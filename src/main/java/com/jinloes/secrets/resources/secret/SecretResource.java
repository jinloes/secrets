package com.jinloes.secrets.resources.secret;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.ResourceSupport;

/**
 * Models a secret resource.
 */
public class SecretResource extends ResourceSupport {
    private UUID id;
    private String secret;

    public SecretResource(UUID id, String secret) {
        this.id = id;
        this.secret = secret;
    }

    @JsonProperty("id")
    public UUID getResourceId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }
}
