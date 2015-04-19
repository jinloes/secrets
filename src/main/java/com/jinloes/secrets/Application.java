package com.jinloes.secrets;

import java.security.Principal;

import com.jinloes.secrets.api.UserRepository;
import com.jinloes.secrets.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.security.config.annotation.authentication.builders
        .AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers
        .GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/**
 * Application configuration. Defines beans and/or configuration for the project.
 */
@SpringBootApplication

@EnableResourceServer
public class Application extends RepositoryRestMvcConfiguration {

    public static final int HASH_WORK_FACTOR = 15;

    /**
     * Main method to start the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    public void init(UserRepository userRepository) {
        User user = new User("user@email.com", "Joe", "Somebody",
                "$2a$15$9lMwQaD/OPMnru.W3fEQU.O2jXXCuOOz9fVUH5CMbc7m1MXrU9yTm"); // pw = password
        userRepository.save(user);
    }

    @Autowired
    private DefaultTokenServices defaultTokenServices;

    @RequestMapping(value = "/oauth/revoke", method = RequestMethod.POST)
    public void create(@RequestParam("token") String value) throws InvalidClientException {
        defaultTokenServices.revokeToken(value);
    }

    @Configuration
    public static class GlobalAuthtenicationConfiguration extends
            GlobalAuthenticationConfigurerAdapter {
        @Autowired private UserDetailsService userDetailsService;
        @Autowired private PasswordEncoder passwordEncoder;
        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(HASH_WORK_FACTOR);
    }
}
