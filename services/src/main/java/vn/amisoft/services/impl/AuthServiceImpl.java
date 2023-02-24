package vn.amisoft.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.amisoft.common.auth.LoginRequest;
import vn.amisoft.common.auth.UserDetailsImpl;
import vn.amisoft.common.models.JsonWebToken;
import vn.amisoft.services.AuthService;
import vn.amisoft.services.JsonWebTokenService;

@Service
public class AuthServiceImpl implements AuthService,AuthenticationManager{
    private final UserDetailsService userDetailsService;
    private final JsonWebTokenService jsonWebTokenService;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserDetailsService userDetailsService, JsonWebTokenService jsonWebTokenService, PasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.jsonWebTokenService = jsonWebTokenService;
        this.encoder = encoder;
    }

    @Override
    public JsonWebToken login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jsonWebTokenService.create(authentication);
    }

    @Override
    public JsonWebToken refresh(String refreshToken) throws AuthenticationException {
        JsonWebToken jsonWebToken = jsonWebTokenService.findAndValidateRefreshToken(refreshToken);
        UserDetails userDetails = new UserDetailsImpl(jsonWebToken.getUser());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        jsonWebTokenService.remove(jsonWebToken);
        return jsonWebTokenService.create(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetail = userDetailsService.loadUserByUsername(authentication.getName());
        if (!encoder.matches(authentication.getCredentials().toString(), userDetail.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        return new UsernamePasswordAuthenticationToken(userDetail, userDetail.getPassword(), userDetail.getAuthorities());
    }
}
