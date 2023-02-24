package vn.amisoft.dao.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import vn.amisoft.common.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "user", indexes = @Index(name = "user_username_index", columnList = "username", unique = true))
public class UserEntity extends BaseEntity<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status",columnDefinition="tinyint(1) default 0")
    private Boolean status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;


    @OneToMany(mappedBy = "userEntity")
    private List<JsonWebTokenEntity> jsonWebTokenEntities;

    @Override
    public User toModel() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setStatus(status);
        user.setFullName(fullName);
        user.setCreatedAt(createdAt);
        user.setCreatedBy(createdBy);
        return user;
    }

    @Override
    public void ofModel(User user) {
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        status = user.getStatus();
        fullName = user.getFullName();
        createdAt = user.getCreatedAt();
        createdBy = user.getCreatedBy();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}