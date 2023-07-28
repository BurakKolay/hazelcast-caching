package com.burakkolay.hazelcastspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HazelcastSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastSpringBootApplication.class, args);
    }

}
