/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Random;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author rakhadjo
 */
@Entity
@Table(name = "api_key")
public class APIKey {

    private static Random rand = new Random();

    private static final String VALID_API_KEY_REGEX
            = "^[A-Za-z0-9]{8}-[A-Za-z0-9]{8}-[A-Za-z0-9]{8}-[A-Za-z0-9]{8}$";

    public static boolean isValidKey(String key) {
        return Pattern.matches(VALID_API_KEY_REGEX, key);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    private Integer id;

    @Column(name = "token")
    private String token;

    public APIKey() {
        this.token = keyGen();
    }

    private static String keyGen() {
        String newKey = "";
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                int num = rand.nextInt(charset.length());
                newKey += charset.charAt(num);
            }
            newKey += "-";
        }
        for (int j = 0; j < 8; j++) {
            int num = rand.nextInt(charset.length());
            newKey += charset.charAt(num);
        }
        return newKey;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.token = key;
    }

    public Integer getId() {
        return this.id;
    }

    public String getKey() {
        return this.token;
    }

    public org.bson.Document toJSON() {
        return new org.bson.Document()
                .append("key", this.token);
    }

}
