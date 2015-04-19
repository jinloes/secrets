package com.jinloes.secrets;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration
        .WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration
        .ResourceServerConfigurerAdapter;

/**
 * Configures the web security for the application.
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.anonymous().and()
                .authorizeRequests()
                .antMatchers("/index.html", "/home.html", "/login.html", "/webjars/**", "/",
                        "/js/**", "/oauth/**").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Configuration
    public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.anonymous().and()
                    .authorizeRequests()
                    .antMatchers("/index.html ", "/home.html", "/login.html", "/webjars/**", "/",
                            "/js/**", "/oauth/**").permitAll()
                    .anyRequest().authenticated();
        }
    }
}
