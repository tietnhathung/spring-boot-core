package vn.amisoft.services.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.amisoft.common.auth.UserDetailsImpl;
import vn.amisoft.common.models.JsonWebToken;
import vn.amisoft.common.models.types.JsonWebTokenType;
import vn.amisoft.dao.entities.JsonWebTokenEntity;
import vn.amisoft.dao.entities.UserEntity;
import vn.amisoft.dao.repositories.JsonWebTokenRepository;
import vn.amisoft.services.JsonWebTokenService;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Log4j2
@Service
public class JsonWebTokenServiceImpl implements JsonWebTokenService {
    private final JsonWebTokenRepository jsonWebTokenRepository;
    @Value("${app.jwt-expiration-ms}")
    private int jwtExpirationMs;
    @Value("${app.jwt-refresh-expiration-ms}")
    private int jwtRefreshExpirationMs;
    @Value("${app.secret-string}")
    private String secretString;

    public JsonWebTokenServiceImpl(JsonWebTokenRepository jsonWebTokenRepository) {
        this.jsonWebTokenRepository = jsonWebTokenRepository;
    }
    public SecretKey key(){
       return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    }
    @Override
    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .claim("userDetails", userPrincipal)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    @Override
    public String generateJwtRefreshToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
                .signWith(key())
                .compact();
    }

    @Override
    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(jwt);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    @Override
    public String getUserNameFromJwtToken(String jwt){
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(jwt).getBody().getSubject();
    }

    @Override
    public JsonWebToken create(Authentication authentication) {
        UserDetailsImpl  userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserEntity user = new UserEntity(userDetails.getUser());
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
        JsonWebTokenEntity token = new JsonWebTokenEntity(jwt);
        jsonWebTokenRepository.delete(token);
    }

    @Override
    public String parseJwt(String fullToken) {
        if (!StringUtils.hasText(fullToken)){
            return null;
        }
        if (fullToken.startsWith(JsonWebTokenType.BEARER.getValue())) {
            return fullToken.substring(7);
        }
        return null;
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
