package com.authorizationserver.web;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String oauth2LoginPage(Model model,
                                  @RequestAttribute(name = "org.springframework.security.web.csrf.CsrfToken", required = false) CsrfToken csrfToken) {

        if (csrfToken != null) {
            model.addAttribute("_csrfToken", csrfToken);
        }
        return "login-page";
    }
}
