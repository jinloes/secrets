package com.jinloes.secrets.resources.user;

import com.jinloes.secrets.model.User;
import com.jinloes.secrets.web.UserController;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Constructs {@link UserResource} objects.
 */
@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {

    public UserResourceAssembler() {
        super(UserController.class, UserResource.class);
    }

    @Override
    public UserResource toResource(User entity) {
        UserResource resource = new UserResource(entity.getUsername(), entity.getFirstName(),
                entity.getId(), entity.getLastName());
        resource.add(linkTo(methodOn(UserController.class).user(null)).withSelfRel());
        return resource;
    }
}
