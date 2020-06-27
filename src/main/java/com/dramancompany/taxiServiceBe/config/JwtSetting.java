package com.dramancompany.taxiServiceBe.config;

import org.springframework.beans.factory.annotation.Value;

public interface JwtSetting {
    String SECRET_KEY = "hussard015";
    Long EXPIRATION_TIME = 1000 * 60 * 60L;
}
