package vn.amisoft.services.impl;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.amisoft.common.auth.UserDetailsImpl;
import vn.amisoft.common.models.JsonWebToken;
import vn.amisoft.common.models.types.JsonWebTokenType;
import vn.amisoft.dao.entities.JsonWebTokenEntity;
import vn.amisoft.dao.entities.UserEntity;
import vn.amisoft.dao.repositories.JsonWebTokenRepository;
import vn.amisoft.services.JsonWebTokenService;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class JsonWebTokenServiceImpl implements JsonWebTokenService {
    private final JsonWebTokenRepository jsonWebTokenRepository;
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public JsonWebTokenServiceImpl(JsonWebTokenRepository jsonWebTokenRepository) {
        this.jsonWebTokenRepository = jsonWebTokenRepository;
    }

    @Override
    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .claim("userDetails", userPrincipal)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    @Override
    public String generateJwtRefreshToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean validateJwtToken(String jwt) {
        return false;
    }

    @Override
    public String getUserNameFromJwtToken(String jwt) {
        return null;
    }

    @Override
    public JsonWebToken create(Authentication authentication) {
        UserDetailsImpl  userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserEntity user = new UserEntity();
        user.ofModel(userDetails.getUser());
        String token = generateJwtToken(authentication);
        String refreshToken = generateJwtRefreshToken(authentication);
        JsonWebTokenEntity jsonWebToken = new JsonWebTokenEntity();
        jsonWebToken.setType(JsonWebTokenType.BEARER);
        jsonWebToken.setAccessToken(token);
        jsonWebToken.setRefreshToken(refreshToken);
        jsonWebToken.setUserEntity(user);
        jsonWebToken.setCreatedAt(LocalDateTime.now());
        jsonWebTokenRepository.save(jsonWebToken);
        return jsonWebToken.toModel();
    }

    @Override
    public void remove(JsonWebToken jwt) {
        jsonWebTokenRepository.delete(JsonWebTokenEntity.of(jwt));
    }

    @Override
    public JsonWebToken findAndValidateJwtToken(String token) {
        if (validateJwtToken(token)){
            Optional<JsonWebTokenEntity> jsonWebTokenEntity =  jsonWebTokenRepository.findByAccessToken(token);
            if (jsonWebTokenEntity.isPresent()){
                return jsonWebTokenEntity.get().toModel();
            }
        }
        throw new JwtException("token failure");
    }

    @Override
    public JsonWebToken findAndValidateRefreshToken(String refreshToken) {
        if (validateJwtToken(refreshToken)){
            Optional<JsonWebTokenEntity> jsonWebTokenEntity =  jsonWebTokenRepository.findByRefreshToken(refreshToken);
            if (jsonWebTokenEntity.isPresent()){
                return jsonWebTokenEntity.get().toModel();
            }
        }
        throw new JwtException("refresh token failure");
    }
}
