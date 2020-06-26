package com.dramancompany.taxiServiceBe.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dramancompany.taxiServiceBe.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends BasicAuthenticationFilter {

    private final String HEADER_STRING = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    @Value("${security.jwt.secret_key}")
    private String SECRET_KEY;

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // endpoint every request hit with authorization
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Read the Authorization header, where the JWT Token should be
        String header = request.getHeader(HEADER_STRING);

        // If header does not contain BEARER or is null delegate to Spring impl and exit
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            // rest of the spring pipeline
            chain.doFilter(request, response);
            return;
        }

        // If header is present, try grab user principal from database and perform authorization
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue filter execution
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token and validate it (decode)
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));

            try {
                User user = User
                        .builder()
                        .id(jwt.getClaim("userId").asLong())
                        .username(jwt.getSubject())
                        .userType(User.UserType.of(jwt.getClaim("userType").asInt()))
                        .build();

                return new UsernamePasswordAuthenticationToken(user, null, null);
            } catch (JWTVerificationException exception) {
                return null;
            }
        }
        return null;
    }
}
