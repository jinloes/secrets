package com.jinloes.secrets;

import java.net.URI;

import com.jayway.restassured.RestAssured;
import com.jinloes.secrets.api.SecretRepository;
import com.jinloes.secrets.api.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.util.UriComponentsBuilder;

import static org.hamcrest.CoreMatchers.is;

/**
 * Integration tests for {@link com.jinloes.secrets.web.UserController}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class UserControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired private UserRepository userRepository;
    @Autowired private SecretRepository secretRepository;
    @Autowired private TextEncryptor encryptor;
    @Value("${local.server.port}")
    private int port;
    private URI baseUri;

    @Before
    public void setUp() {
        initializeUsers(userRepository);
        initializeSecrets(secretRepository, encryptor);
        baseUri = UriComponentsBuilder.fromHttpUrl("http://localhost")
                .port(port)
                .build().toUri();
        RestAssured.baseURI = baseUri.toString();
    }

    @Test
    public void testGetCurrentUser() throws Exception {
        OAuth2AccessToken accessToken = getAccessToken(baseUri, "user@email.com", "password");
        RestAssured.given()
                .header("Authorization", "Bearer " + accessToken.getValue())
                .when()
                .get("/users")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.OK.value())
                .body("first_name", is("Joe"))
                .body("last_name", is("Somebody"))
                .body("email", is("user@email.com"));
    }

    @Test
    public void testGetUserSecrets() throws Exception {
        OAuth2AccessToken accessToken = getAccessToken(baseUri, "user@email.com", "password");
        RestAssured.given()
                .header("Authorization", "Bearer " + accessToken.getValue())
                .when()
                .get("/users/{userId}/secrets", user.getId())
                .then()
                .log().ifError()
                .statusCode(HttpStatus.OK.value())
                .body("_embedded.secretResources[0].secret", is("secret1"))
                .body("_embedded.secretResources[1].secret", is("secret2"));
    }

    @Test
    public void testGetUserSecretsInvalidAuthorization() throws Exception {
        OAuth2AccessToken accessToken = getAccessToken(baseUri, "user2@email.com", "password");
        RestAssured.given()
                .header("Authorization", "Bearer " + accessToken.getValue())
                .when()
                .get("/users/{userId}/secrets", user.getId())
                .then()
                .log().ifError()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
