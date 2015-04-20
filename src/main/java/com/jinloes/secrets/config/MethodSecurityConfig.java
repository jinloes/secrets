package com.jinloes.secrets.config;


import com.jinloes.secrets.service.impl.DelegatingPermissionEvaluator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration
        .EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration
        .GlobalMethodSecurityConfiguration;


/**
 * Method security configuration.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Autowired private ApplicationContext applicationContext;
    @Autowired private DelegatingPermissionEvaluator permissionEvaluator;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new
                DefaultMethodSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }
}
