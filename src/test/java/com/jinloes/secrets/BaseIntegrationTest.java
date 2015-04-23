package com.jinloes.secrets;

import java.net.URI;

import com.google.common.collect.Lists;
import com.jinloes.secrets.repositories.api.SecretRepository;
import com.jinloes.secrets.repositories.api.UserRepository;
import com.jinloes.secrets.model.Secret;
import com.jinloes.secrets.model.User;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password
        .ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Base integration test. Common functions used between tests.
 */
public class BaseIntegrationTest {
    protected User user;
    protected User user2;
    protected Secret secret;
    protected Secret secret2;

    /**
     * Sets up users for test.
     *
     * @param userRepository user repository
     */
    protected void initializeUsers(UserRepository userRepository) {
        userRepository.deleteAll();
        user = new User("user@email.com", "Joe", "Somebody",
                "$2a$15$9lMwQaD/OPMnru.W3fEQU.O2jXXCuOOz9fVUH5CMbc7m1MXrU9yTm"); // pw = password
        user.setId("ddc1f9f3-2e8e-47ce-b388-27624d964288");
        user2 = new User("user2@email.com", "John", "Doe",
                "$2a$15$9lMwQaD/OPMnru.W3fEQU.O2jXXCuOOz9fVUH5CMbc7m1MXrU9yTm"); // pw = password
        user2.setId("17bedad6-df95-4adc-87ee-de9350f1daf4");
        userRepository.insert(Lists.newArrayList(user, user2));
    }

    /**
     * Initializes secrets.
     *
     * @param secretRepository secret repository
     * @param encryptor        encryptor
     */
    protected void initializeSecrets(SecretRepository secretRepository, TextEncryptor encryptor) {
        secretRepository.deleteAll();
        secret = new Secret(encryptor.encrypt("secret1"));
        secret.setCreatedBy(user.getId());
        secret2 = new Secret(encryptor.encrypt("secret2"));
        secret2.setCreatedBy(user.getId());
        secretRepository.save(Lists.newArrayList(secret, secret2));
    }

    /**
     * Gets an access token for authentication.
     *
     * @param baseUri  base uri
     * @param username username
     * @param password password
     */
    protected OAuth2AccessToken getAccessToken(URI baseUri, String username, String password) {
        ResourceOwnerPasswordResourceDetails passwordResourceDetails =
                new ResourceOwnerPasswordResourceDetails();
        passwordResourceDetails.setUsername(username);
        passwordResourceDetails.setPassword(password);
        passwordResourceDetails.setAccessTokenUri(UriComponentsBuilder.fromUri(baseUri)
                .pathSegment("oauth", "token").toUriString());
        passwordResourceDetails.setClientId("acme");
        passwordResourceDetails.setClientSecret("acmesecret");
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(passwordResourceDetails,
                new DefaultOAuth2ClientContext());
        return restTemplate.getAccessToken();
    }

}
