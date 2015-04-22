package com.jinloes.secrets.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ResourceSupport;

/**
 * Models a response from modifying (creating/updating) an object.
 */
public class ModifiedResourceResponse extends ResourceSupport {
    private String id;

    public ModifiedResourceResponse(String id, EntityLinks entityLinks, Class<?> resourceClass) {
        this.id = id;
        add(entityLinks.linkToSingleResource(resourceClass, id).withSelfRel());
    }

    @JsonProperty("id")
    public String getResourceId() {
        return id;
    }
}
