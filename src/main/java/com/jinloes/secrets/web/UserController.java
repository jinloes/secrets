package com.jinloes.secrets.web;

import java.util.UUID;

import com.jinloes.secrets.api.SecretRepository;
import com.jinloes.secrets.api.UserRepository;
import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;
import com.jinloes.secrets.resources.user.UserResource;
import com.jinloes.secrets.resources.user.UserResourceAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for interacting with {@link User} objects.
 */
@RestController
@ExposesResourceFor(User.class)
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final SecretRepository secretRepository;
    private final UserResourceAssembler userResourceAssembler;

    @Autowired
    public UserController(UserRepository userRepository, SecretRepository secretRepository,
            UserResourceAssembler userResourceAssembler) {
        this.userRepository = userRepository;
        this.secretRepository = secretRepository;
        this.userResourceAssembler = userResourceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET)
    public UserResource user(@AuthenticationPrincipal User currentUser) {
        return userResourceAssembler.toResource(currentUser);
    }

    @RequestMapping(value = "/{userId}/secrets", method = RequestMethod.GET)
    public Page<Secret> getSecrets(@PathVariable("userId") UUID userId,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        User user = userRepository.getOne(userId);
        return secretRepository.findByCreatedBy(userId, pageable);
    }
}
