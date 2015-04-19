package com.jinloes.secrets.web;

import com.jinloes.secrets.model.User;
import com.jinloes.secrets.resources.user.UserResource;
import com.jinloes.secrets.resources.user.UserResourceAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for interacting with {@link User} objects.
 */
@RestController
@ExposesResourceFor(User.class)
public class UserController {
    private final UserResourceAssembler userResourceAssembler;

    @Autowired
    public UserController(UserResourceAssembler userResourceAssembler) {
        this.userResourceAssembler = userResourceAssembler;
    }

    @RequestMapping("/user")
    public UserResource user(@AuthenticationPrincipal User currentUser) {
        return userResourceAssembler.toResource(currentUser);
    }
}
