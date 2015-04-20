package com.jinloes.secrets.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adds custom OAuth2 endpoints.
 */
@RestController
@RequestMapping("/oauth")
public class OAuthController {
    private DefaultTokenServices defaultTokenServices;

    @Autowired
    public OAuthController(DefaultTokenServices defaultTokenServices) {
        this.defaultTokenServices = defaultTokenServices;
    }

    /**
     * Revokes an oauth token.
     *
     * @param value token to revoke
     * @throws InvalidClientException
     */
    @RequestMapping(value = "/revoke", method = RequestMethod.POST)
    public void create(@RequestParam("token") String value) throws InvalidClientException {
        defaultTokenServices.revokeToken(value);
    }
}
