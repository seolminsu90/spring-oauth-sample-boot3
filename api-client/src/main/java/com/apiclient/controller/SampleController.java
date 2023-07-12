package com.apiclient.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class SampleController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WebClient webClient;

    public SampleController(WebClient webClient, OAuth2AuthorizedClientService authorizedClientService) {
        this.webClient = webClient;
        this.authorizedClientService = authorizedClientService;
    }

    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/sample")
    public String test(@RegisteredOAuth2AuthorizedClient("sample-authorization-code") OAuth2AuthorizedClient authorizedClient) {
        return this.webClient
                .get()
                .uri("http://127.0.0.1:8090/sample")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }

    @GetMapping("/user")
    public OAuth2AuthorizedClient test2(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken =
                (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                "sample-oidc", oauthToken.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken().getTokenValue();

        logger.info("AccessToken : {}", accessToken);
        logger.info("RefreshToken : {}", refreshToken);
        return client;
    }
}
