package com.jinloes.secrets.service.api;

import java.io.Serializable;
import java.util.Objects;

import com.jinloes.secrets.model.AuditedEntity;
import com.jinloes.secrets.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

/**
 *
 */
public abstract class BasePermissionEvaluator<T extends AuditedEntity>
        implements PermissionEvaluator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasePermissionEvaluator.class);

    /**
     * Returns true if the user created the {@link AuditedEntity} otherwise, false.
     *
     * @param entity audited entity
     * @param user   current user
     * @return true if the user created the entity
     */
    public boolean isCreator(AuditedEntity entity, User user) {
        return Objects.equals(entity.getCreatedBy(), user.getId());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject,
            Object permission) {
        try {
            @SuppressWarnings("unchecked")
            T targetObject = (T) targetDomainObject;
            User principal = (User) authentication.getPrincipal();
            return targetDomainObject == null || hasPermission(principal, targetObject, permission);
        } catch (ClassCastException e) {
            LOGGER.error("Target object could not be cast to specific type", e);
            return false;
        }
    }

    /**
     * Execute the permission check
     *
     * @param principal    principal
     * @param targetObject target object
     * @param permission   permission
     * @return true if the user has permission otherwise false
     */
    protected abstract boolean hasPermission(User principal, T targetObject, Object permission);

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
            String targetType, Object permission) {
        return false;
    }
}
