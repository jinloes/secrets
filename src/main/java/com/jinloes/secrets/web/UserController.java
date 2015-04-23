package com.jinloes.secrets.web;

import com.jinloes.secrets.repositories.api.UserRepository;
import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;
import com.jinloes.secrets.resources.secret.SecretResource;
import com.jinloes.secrets.resources.secret.SecretResourceAssembler;
import com.jinloes.secrets.resources.user.UserResource;
import com.jinloes.secrets.resources.user.UserResourceAssembler;
import com.jinloes.secrets.service.api.SecretService;
import com.jinloes.secrets.util.RestPreconditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
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
    private final SecretService secretService;
    private final UserResourceAssembler userResourceAssembler;
    private final SecretResourceAssembler secretResourceAssembler;

    @Autowired
    public UserController(UserRepository userRepository, SecretService secretService,
            UserResourceAssembler userResourceAssembler,
            SecretResourceAssembler secretResourceAssembler) {
        this.userRepository = userRepository;
        this.secretService = secretService;
        this.userResourceAssembler = userResourceAssembler;
        this.secretResourceAssembler = secretResourceAssembler;
    }

    /**
     * Returns the currently logged in user.
     *
     * @param currentUser currently logged in user
     * @return currently logged in user
     */
    @RequestMapping(method = RequestMethod.GET)
    public UserResource user(@AuthenticationPrincipal User currentUser) {
        return userResourceAssembler.toResource(currentUser);
    }

    /**
     * Returns a user's secrets.
     *
     * @param userId    user id  to retrieve secrets for
     * @param pageable  pageable object, determines the page size, sort, etc
     * @param assembler paged resources assembler
     * @return secret resource page
     */
    @RequestMapping(value = "/{userId}/secrets", method = RequestMethod.GET)
    public PagedResources<SecretResource> getSecrets(@PathVariable("userId") String userId,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            PagedResourcesAssembler<Secret> assembler) {
        User user = userRepository.findOne(userId);
        RestPreconditions.checkNotNull(user);
        return assembler.toResource(secretService.getByCreatedBy(user, pageable),
                secretResourceAssembler);
    }
}
