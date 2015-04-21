package com.jinloes.secrets.web;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ResourceSupport;

/**
 * Models a response from creating an object.
 */
public class CreatedResourceResponse extends ResourceSupport {
    private UUID id;

    public CreatedResourceResponse(UUID id, EntityLinks entityLinks, Class<?> resourceClass) {
        this.id = id;
        add(entityLinks.linkToSingleResource(resourceClass, id).withSelfRel());
    }

    @JsonProperty("id")
    public UUID getResourceId() {
        return id;
    }
}
