package com.jinloes.secrets.resources.secret;

import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.web.SecretController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A resource assembler for creating {@link SecretResource} objects.
 */
@Component
public class SecretResourceAssembler extends ResourceAssemblerSupport<Secret, SecretResource> {
    private final TextEncryptor encryptor;

    @Autowired
    public SecretResourceAssembler(TextEncryptor encryptor) {
        super(SecretController.class, SecretResource.class);
        this.encryptor = encryptor;
    }

    @Override
    public SecretResource toResource(Secret entity) {
        SecretResource resource = new SecretResource(entity.getId(),
                encryptor.decrypt(entity.getSecret()));
        resource.add(linkTo(methodOn(SecretController.class).getSecret(entity.getId()))
                .withSelfRel());
        return resource;
    }
}
