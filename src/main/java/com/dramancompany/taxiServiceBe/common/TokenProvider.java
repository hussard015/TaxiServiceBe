package com.dramancompany.taxiServiceBe.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dramancompany.taxiServiceBe.config.JwtSetting;
import com.dramancompany.taxiServiceBe.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {

    public String createToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("userId", user.getId())
                .withClaim("userType", user.getUserType().ordinal())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtSetting.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtSetting.SECRET_KEY));
    }
}
