package vn.amisoft.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.amisoft.dao.entities.JsonWebTokenEntity;

import java.util.Optional;

public interface JsonWebTokenRepository extends JpaRepository<JsonWebTokenEntity, Integer> {

    @Query(value = "select token.* from json_web_token as token where token.refresh_token = :refreshToken limit 1",nativeQuery = true)
    Optional<JsonWebTokenEntity> findByRefreshToken(@Param("refreshToken") String refreshToken);

    @Query(value = "select token.* from json_web_token as token where token.access_token = :token  limit 1",nativeQuery = true)
    Optional<JsonWebTokenEntity> findByAccessToken(@Param("token") String token);
}