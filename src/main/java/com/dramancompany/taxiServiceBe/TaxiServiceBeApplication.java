package com.dramancompany.taxiServiceBe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaxiServiceBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxiServiceBeApplication.class, args);
    }

}
