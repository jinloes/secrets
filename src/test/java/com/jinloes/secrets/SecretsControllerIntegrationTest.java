package com.jinloes.secrets;

import java.net.URI;
import java.util.UUID;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jinloes.secrets.api.SecretRepository;
import com.jinloes.secrets.api.UserRepository;
import com.jinloes.secrets.model.Secret;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Integration tests for {@link com.jinloes.secrets.web.SecretsController}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class SecretsControllerIntegrationTest extends BaseIntegrationTest {
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
    @Transactional
    public void testCreateSecret() throws Exception {
        OAuth2AccessToken accessToken = getAccessToken(baseUri, "user@email.com", "password");
        String secretId = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken.getValue())
                .body(IOUtils.toString(new ClassPathResource("json/create_secret.json")
                        .getInputStream()))
                .contentType(ContentType.JSON)
                .when()
                .post("/secrets")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().get("id");
        Secret createdSecret = secretRepository.getOne(UUID.fromString(secretId));
        assertThat(createdSecret, notNullValue());
        assertThat(encryptor.decrypt(createdSecret.getSecret()), is("mySecret"));
    }

    @Test
    public void testGetSecret() throws Exception {
        OAuth2AccessToken accessToken = getAccessToken(baseUri, "user@email.com", "password");
        RestAssured.given()
                .header("Authorization", "Bearer " + accessToken.getValue())
                .when()
                .get("/secrets/{secretId}", secret.getId())
                .then()
                .log().ifError()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(secret.getId().toString()))
                .body("secret", is(encryptor.decrypt(secret.getSecret())));
    }

    @Test
    public void testGetSecretNotFound() throws Exception {
        OAuth2AccessToken accessToken = getAccessToken(baseUri, "user@email.com", "password");
        RestAssured.given()
                .header("Authorization", "Bearer " + accessToken.getValue())
                .when()
                .get("/secrets/{secretId}", UUID.randomUUID())
                .then()
                .log().ifError()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testGetSecretInvalidAuthorization() throws Exception {
        OAuth2AccessToken accessToken = getAccessToken(baseUri, "user2@email.com", "password");
        RestAssured.given()
                .header("Authorization", "Bearer " + accessToken.getValue())
                .when()
                .get("/secrets/{secretId}", secret.getId())
                .then()
                .log().ifError()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
