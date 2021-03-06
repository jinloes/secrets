package com.jinloes.secrets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Models a secret
 */
@Document
public class Secret extends AuditedEntity {
    private String secret;

    public Secret() {
    }

    @JsonCreator
    public Secret(@JsonProperty("secret") String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
