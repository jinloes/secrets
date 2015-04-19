package com.jinloes.secrets.web;

import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;

import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for interacting with {@link Secret} objects.
 */
@RestController
@ExposesResourceFor(Secret.class)
@RequestMapping("/secrets")
public class SecretController {
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreatedResourceResponse create(@RequestBody Secret secret,
            @AuthenticationPrincipal User currentUser) {
        return null;
    }
}
