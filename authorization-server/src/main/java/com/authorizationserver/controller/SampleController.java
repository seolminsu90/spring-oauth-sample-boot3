package com.authorizationserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    @GetMapping("/")
    public String root(@AuthenticationPrincipal UserDetails userDetails) {
        return "ROOT ::: " + userDetails.getUsername();
    }
}
