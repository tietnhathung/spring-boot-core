package vn.amisoft.application.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.amisoft.common.auth.UserDetailsImpl;
import vn.amisoft.common.models.JsonWebToken;
import vn.amisoft.services.JsonWebTokenService;
import vn.amisoft.services.impl.UserDetailsServiceImpl;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JsonWebTokenService jsonWebTokenService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws AuthenticationException, ServletException, IOException {
        try {
            String jwtString = jsonWebTokenService.parseJwt(request.getHeader("Authorization"));
            if (jwtString != null){
                JsonWebToken jsonWebToken = jsonWebTokenService.findAndValidateJwtToken(jwtString);
                UserDetails userDetails = new UserDetailsImpl(jsonWebToken.getUser());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
