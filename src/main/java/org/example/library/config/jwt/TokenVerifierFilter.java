package org.example.library.config.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.example.library.service.AuthUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenVerifierFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final AuthUserDetailsService authUserDetailsService;

    public TokenVerifierFilter(JwtUtils jwtUtils, AuthUserDetailsService authUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.authUserDetailsService = authUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = httpServletRequest.getHeader( "Authorization" );
        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith( "Bearer " )) {
            String jwt = authHeader.substring( 7 );
            if(jwt.isBlank()) {
                httpServletResponse.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Bearer Header"
                );
            } else {
                try {
                    String username = jwtUtils.validateTokenAndRetrieveClaim( jwt );
                    UserDetails userDetails = authUserDetailsService.loadUserByUsername( username );
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken( userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities() );
                    if(SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication( authToken );
                    }
                } catch (JWTVerificationException exc) {
                    httpServletResponse.sendError( HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token" );
                }
            }
        }
        filterChain.doFilter( httpServletRequest, httpServletResponse );
    }

}
