package com.jinloes.secrets.service.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.jinloes.secrets.model.AuditedEntity;
import com.jinloes.secrets.model.User;
import com.jinloes.secrets.service.api.BasePermissionEvaluator;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Permission evaluator that will delegate to a specific permission evaluator if one exists.
 */
@Component
public class DelegatingPermissionEvaluator implements PermissionEvaluator {
    private static final Map<Class, Class<? extends PermissionEvaluator>> CLASS_TO_EVALUATOR =
            ImmutableMap.<Class, Class<? extends PermissionEvaluator>>builder()
                    .put(User.class, UserPermissionEvaluator.class)
                    .build();
    private final ApplicationContext applicationContext;

    @Autowired
    public DelegatingPermissionEvaluator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject,
            Object permission) {
        if (targetDomainObject == null) {
            return true;
        }
        // Hibernate proxies objects for lazy initialization we need to get the true class
        PermissionEvaluator delegate = getEvaluator(Hibernate.getClass(targetDomainObject));
        return delegate.hasPermission(authentication, targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
            String targetType, Object permission) {
        return false;
    }

    private PermissionEvaluator getEvaluator(Class<?> objectClass) {
        Class<? extends PermissionEvaluator> evaluatorClass = CLASS_TO_EVALUATOR.get(objectClass);
        // Set a default if there is no object mapping
        if (evaluatorClass == null) {
            evaluatorClass = DefaultPermissionEvaluator.class;
        }
        return applicationContext.getBean(evaluatorClass);
    }

    @Component
    @VisibleForTesting
    static class DefaultPermissionEvaluator extends BasePermissionEvaluator {
        @Override
        protected boolean hasPermission(User principal, AuditedEntity targetObject, Object
                permission) {
            switch (Objects.toString(permission)) {
                case "read":
                case "update":
                case "delete":
                    return isCreator(targetObject, principal);
                default:
                    return false;
            }
        }
    }
}

