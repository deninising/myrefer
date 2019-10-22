package com.dennis.mysso.server.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dennis.mysso.common")
public class MyssoServerRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyssoServerRedisApplication.class, args);
    }

}
