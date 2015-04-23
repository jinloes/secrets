package com.jinloes.secrets.resources.secret;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.ResourceSupport;

/**
 * Models a secret resource.
 */
public class SecretResource extends ResourceSupport {
    private String id;
    private String secret;

    public SecretResource(String id, String secret) {
        this.id = id;
        this.secret = secret;
    }

    @JsonProperty("id")
    public String getResourceId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }
}
