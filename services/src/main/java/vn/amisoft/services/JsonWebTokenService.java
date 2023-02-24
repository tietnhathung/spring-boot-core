package vn.amisoft.services;

import org.springframework.security.core.Authentication;
import vn.amisoft.common.models.JsonWebToken;

public interface JsonWebTokenService {
    String generateJwtToken(Authentication authentication);

    String generateJwtRefreshToken(Authentication authentication);

    boolean validateJwtToken(String jwt);

    String getUserNameFromJwtToken(String jwt);

    JsonWebToken create(Authentication authentication);

    void remove(JsonWebToken jwt);

    JsonWebToken findAndValidateJwtToken(String refreshToken);

    JsonWebToken findAndValidateRefreshToken(String refreshToken);
}
