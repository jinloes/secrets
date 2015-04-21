package com.jinloes.secrets;

import java.util.UUID;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jinloes.secrets.api.UserRepository;
import com.jinloes.secrets.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.security.config.annotation.authentication.builders
        .AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers
        .GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

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
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        User user = new User("user@email.com", "Joe", "Somebody",
                "$2a$15$9lMwQaD/OPMnru.W3fEQU.O2jXXCuOOz9fVUH5CMbc7m1MXrU9yTm"); // pw = password
        userRepository.save(user);
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

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
    }

    @Bean
    public TextEncryptor encryptor() {
        // Requires unlimited strength JCE to be installed:
        // http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
        // http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
        return Encryptors.text("mySecret", "abc123");
    }
}
