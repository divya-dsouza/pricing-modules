package org.divyad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.divyad")
@EnableJpaRepositories
public class PriceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PriceApplication.class, args);
    }
}
