package com.softieas.phones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PhonesApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhonesApplication.class, args);
    }

    @EventListener
    public void dataSeed(ContextRefreshedEvent contextRefreshedEvent) {
        //seeding logic
    }

}
