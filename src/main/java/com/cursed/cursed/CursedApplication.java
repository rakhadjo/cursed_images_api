package com.cursed.cursed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author rakhadjo
 */
@SpringBootApplication
public class CursedApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            SpringApplication.run(CursedApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
