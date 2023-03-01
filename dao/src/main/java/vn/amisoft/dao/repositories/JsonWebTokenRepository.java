package vn.amisoft.dao.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.amisoft.dao.entities.JsonWebTokenEntity;

import java.util.List;
import java.util.Optional;

public interface JsonWebTokenRepository extends JpaRepository<JsonWebTokenEntity, Integer> {
    @EntityGraph(attributePaths = "userEntity")
    Optional<JsonWebTokenEntity> findFirstByRefreshToken(@Param("refreshToken") String refreshToken);

    @EntityGraph(attributePaths = "userEntity")
    Optional<JsonWebTokenEntity> findFirstByAccessToken(@Param("accessToken") String accessToken);
}