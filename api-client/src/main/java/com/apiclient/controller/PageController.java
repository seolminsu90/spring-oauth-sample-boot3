package com.apiclient.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class PageController {


    private final OAuth2AuthorizedClientService authorizedClientService;
    private final WebClient webClient;

    public PageController(WebClient webClient, OAuth2AuthorizedClientService authorizedClientService) {
        this.webClient = webClient;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/login/redirect")
    public String test() {
        return "index.html";
    }


    @GetMapping("/logged-out")
    public String loggedOut() {
        return "logged-out";
    }
}
