package vn.amisoft.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.amisoft.dao.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query("select user from UserEntity as user where user.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);
}