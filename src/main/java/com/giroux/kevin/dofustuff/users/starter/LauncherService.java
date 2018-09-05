package com.giroux.kevin.dofustuff.users.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Copyright notice
 * LauncherService.java
 * Classe principale pour le lancement du micro-service
 *
 * @author Kévin Giroux
 * 5 septembre 2017
 * @version v1
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({"com.giroux.kevin.dofustuff"})
public class LauncherService {

    /**
     * Lance l'application spring boot
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {
        SpringApplication.run(LauncherService.class, args);
    }

}

