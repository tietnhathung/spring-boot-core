package vn.amisoft.application.controllers;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.amisoft.common.auth.LoginRequest;
import vn.amisoft.common.auth.RefreshTokenRequest;
import vn.amisoft.common.models.JsonWebToken;
import vn.amisoft.services.AuthService;

@Log4j2
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JsonWebToken login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login {}", loginRequest);
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public JsonWebToken refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("refresh {}", refreshTokenRequest);
        return authService.refresh(refreshTokenRequest.getRefreshToken());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        log.info("logout {}", auth);
        authService.logout(auth);
    }
}
