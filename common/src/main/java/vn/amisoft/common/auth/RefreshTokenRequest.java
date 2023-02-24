package vn.amisoft.common.auth;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
