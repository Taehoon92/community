package hoon.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = "username", required = false) String username) {
        log.info("Home Controller - username = {}", username);
        if (username == null) {
            return "home";
        }
        return "home";
    }
}
