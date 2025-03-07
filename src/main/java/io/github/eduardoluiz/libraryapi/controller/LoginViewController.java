package io.github.eduardoluiz.libraryapi.controller;

import io.github.eduardoluiz.libraryapi.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String pageLogin() {
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String pageHome(Authentication authentication) {
        if(authentication instanceof CustomAuthentication customAuthentication) {
            System.out.println(customAuthentication.getUser());
        }
        return "Hello" + authentication.getName();
    }

    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthorizationCode(@RequestParam("code") String code) {
        return code;
    }
}
