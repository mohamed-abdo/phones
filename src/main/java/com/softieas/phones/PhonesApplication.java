package com.softieas.phones;

import com.softieas.phones.domain.entities.Phone;
import com.softieas.phones.domain.models.NumberStatus;
import com.softieas.phones.domain.repositories.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PhoneRepository phoneRepository;

    @EventListener
    public void dataSeed(ContextRefreshedEvent contextRefreshedEvent) {
        if (phoneRepository.count() == 0) {

        }
    }
}
