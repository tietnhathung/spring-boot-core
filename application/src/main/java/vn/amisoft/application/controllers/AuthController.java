package vn.amisoft.application.controllers;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public JsonWebToken login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login {}", loginRequest);
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    public JsonWebToken refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("refresh {}", refreshTokenRequest);
        return authService.refresh(refreshTokenRequest.getRefreshToken());
    }
}
