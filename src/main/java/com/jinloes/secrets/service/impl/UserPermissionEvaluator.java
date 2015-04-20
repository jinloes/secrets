package com.jinloes.secrets.service.impl;

import java.util.Objects;

import com.jinloes.secrets.model.User;
import com.jinloes.secrets.service.api.BasePermissionEvaluator;

import org.springframework.stereotype.Component;

/**
 * A permission evaluator for {@link User} objects.
 */
@Component
public class UserPermissionEvaluator extends BasePermissionEvaluator<User> {
    @Override
    protected boolean hasPermission(User principal, User targetObject, Object permission) {
        switch (Objects.toString(permission)) {
            case "read-secrets":
                return isSelf(targetObject, principal);
            default:
                return false;
        }
    }

    /**
     * Returns true if the requested user and the current user are the same user.
     *
     * @param entity requested user
     * @param user   current user
     * @return true if requested user is the current user, otherwise false
     */
    public boolean isSelf(User entity, User user) {
        return Objects.equals(entity.getId(), user.getId());
    }
}
