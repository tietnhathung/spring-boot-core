package vn.amisoft.common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseModel{
    private Integer id;

    private String username;

    private String fullName;

    @JsonIgnore
    private String password;

    private Boolean status;

    private LocalDateTime createdAt;

    private Integer createdBy;
}
