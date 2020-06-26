package com.dramancompany.taxiServiceBe.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dramancompany.taxiServiceBe.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {

    @Value("${security.jwt.expire_time}")
    private long EXPIRATION_TIME;
    @Value("${security.jwt.secret_key}")
    private String SECRET_KEY;

    public String createToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("userId", user.getId())
                .withClaim("userType", user.getUserType().ordinal())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }
}
