package com.evergreen.evergreenmedic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.evergreen.evergreenmedic.entities")
public class EvergreenmedicApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvergreenmedicApplication.class, args);
    }

}
