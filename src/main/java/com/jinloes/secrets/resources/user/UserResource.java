package com.jinloes.secrets.resources.user;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.ResourceSupport;

/**
 * Models a user resource object that is returned by the API.
 */
public class UserResource extends ResourceSupport {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;

    @JsonCreator
    public UserResource(@JsonProperty("email") String email,
            @JsonProperty("first_name") String firstName, @JsonProperty("id") UUID id,
            @JsonProperty("last_name") String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("id")
    public UUID getResourceId() {
        return id;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }
}
