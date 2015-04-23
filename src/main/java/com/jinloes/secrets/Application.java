package com.jinloes.secrets;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jinloes.secrets.repositories.api.UserRepository;
import com.jinloes.secrets.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * Application configuration. Defines beans and/or configuration for the project.
 */
@SpringBootApplication
@EnableResourceServer
public class Application extends RepositoryRestMvcConfiguration {
    public static final int HASH_WORK_FACTOR = 15;
    private MongodProcess mongod;
    private int mongoPort;

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
    @Autowired
    private Environment environment;

    /**
     * Bootstraps the application with embedded mongo and a user.
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        // Start embedded mongo
        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(mongoPort, Network.localhostIsIPv6()))
                .build();

        MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
        mongod = mongodExecutable.start();
        // Bootstrap user
        User user = new User("user@email.com", "Joe", "Somebody",
                "$2a$15$9lMwQaD/OPMnru.W3fEQU.O2jXXCuOOz9fVUH5CMbc7m1MXrU9yTm"); // pw = password
        user.setId("ddc1f9f3-2e8e-47ce-b388-27624d964288");
        userRepository.save(user);
    }

    /**
     * Stops embedded mongo
     */
    @PreDestroy
    public void tearDown() {
        if (mongod != null) {
            mongod.stop();
        }
    }

    @Bean
    public MongoFactoryBean mongo() throws IOException {
        mongoPort = Network.getFreeServerPort();
        MongoFactoryBean mongo = new MongoFactoryBean();
        mongo.setHost("localhost");
        mongo.setPort(mongoPort);
        return mongo;
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
