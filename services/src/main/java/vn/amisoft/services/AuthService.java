package vn.amisoft.services;

import org.springframework.security.core.AuthenticationException;
import vn.amisoft.common.auth.LoginRequest;
import vn.amisoft.common.models.JsonWebToken;

public interface AuthService {
    JsonWebToken login(LoginRequest loginRequest);

    JsonWebToken refresh(String refreshToken) throws AuthenticationException;
}
