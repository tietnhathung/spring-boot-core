package vn.amisoft.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import vn.amisoft.common.models.types.JsonWebTokenType;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonWebToken extends BaseModel{
    @JsonIgnore
    private Integer id;
    private JsonWebTokenType type;
    private String accessToken;
    private String refreshToken;
    @JsonIgnore
    private User user;
}

