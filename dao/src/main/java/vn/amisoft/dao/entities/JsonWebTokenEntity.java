package vn.amisoft.dao.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import vn.amisoft.common.models.JsonWebToken;
import vn.amisoft.common.models.types.JsonWebTokenType;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "json_web_token")
public class JsonWebTokenEntity extends BaseEntity<JsonWebToken>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type",columnDefinition = "nvarchar(10)")
    private JsonWebTokenType type;
    @Column(name = "access_token",columnDefinition = "nvarchar(500)")
    private String accessToken;
    @Column(name = "refresh_token",columnDefinition = "nvarchar(500)")
    private String refreshToken;

    @Column(name = "created_at",columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


    public JsonWebTokenEntity(JsonWebToken jsonWebToken) {
        ofModel(jsonWebToken);
    }

    public JsonWebTokenEntity() {

    }

    @Override
    public JsonWebToken toModel() {
        JsonWebToken jwt = new JsonWebToken();
        jwt.setId(id);
        jwt.setType(type);
        jwt.setAccessToken(accessToken);
        jwt.setRefreshToken(refreshToken);
        jwt.setUser(userEntity.toModel());
        return jwt;
    }

    @Override
    public void ofModel(JsonWebToken jsonWebToken) {
        id = jsonWebToken.getId();
        type = jsonWebToken.getType();
        accessToken = jsonWebToken.getAccessToken();
        refreshToken = jsonWebToken.getRefreshToken();
        userEntity = new UserEntity();
        userEntity.ofModel(jsonWebToken.getUser());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        JsonWebTokenEntity that = (JsonWebTokenEntity) o;
        return getAccessToken() != null && Objects.equals(getAccessToken(), that.getAccessToken());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
