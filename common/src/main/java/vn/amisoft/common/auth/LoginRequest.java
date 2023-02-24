package vn.amisoft.common.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "username can not be null")
    private String username;

    @NotBlank(message = "password can not be null")
    private String password;
}
