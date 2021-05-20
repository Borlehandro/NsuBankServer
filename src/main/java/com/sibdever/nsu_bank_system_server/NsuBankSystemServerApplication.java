package com.sibdever.nsu_bank_system_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class NsuBankSystemServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NsuBankSystemServerApplication.class, args);
    }

}
