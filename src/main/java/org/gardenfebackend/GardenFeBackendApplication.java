package org.gardenfebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class GardenFeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GardenFeBackendApplication.class, args);
    }

}
