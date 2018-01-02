package com.myretail.api.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class Application {
    static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) {
        log.info("Starting myretail service ...");
        SpringApplication.run(Application.class, args);
        log.info("Started myretail service ...");
    }
}
