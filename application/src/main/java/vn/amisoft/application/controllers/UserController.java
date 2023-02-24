package vn.amisoft.application.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.amisoft.common.models.User;
import vn.amisoft.services.UserService;

import java.security.Principal;

@Log4j2
@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public User info(Principal principal) {
        log.info("get info {}",principal);
        return userService.findByUsername(principal.getName());
    }
}
