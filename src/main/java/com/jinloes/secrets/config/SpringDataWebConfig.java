package com.jinloes.secrets.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.SpringDataWebConfiguration;

/**
 * Spring data web configuration.
 * See: {@link org.springframework.data.web.config.SpringDataWebConfiguration}
 */
@Configuration
public class SpringDataWebConfig extends SpringDataWebConfiguration {
    @Autowired
    public void configure(PageableHandlerMethodArgumentResolver resolver) {
        resolver.setOneIndexedParameters(true);
    }
}
