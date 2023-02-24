package vn.amisoft.dao.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.amisoft.dao.entities.JsonWebTokenEntity;

import java.util.Optional;

public interface JsonWebTokenRepository extends JpaRepository<JsonWebTokenEntity, Integer> {

    @EntityGraph(attributePaths = "userEntity")
    @Query("select token from JsonWebTokenEntity as token where token.refreshToken = :refreshToken")
    Optional<JsonWebTokenEntity> findByRefreshToken(String refreshToken);

    @EntityGraph(attributePaths = "userEntity")
    @Query("select token from JsonWebTokenEntity as token where token.accessToken = :token")
    Optional<JsonWebTokenEntity> findByAccessToken(String token);
}