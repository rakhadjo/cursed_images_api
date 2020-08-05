/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.misc;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Random;
import java.util.regex.Pattern;

/**
 *
 * @author rakhadjo
 */
@Document(collection = "api_keys")
public class Key {

    private static Random rand = new Random();

    private static final String VALID_EMAIL_ADDRESS_REGEX
            = "^[\\\\w!#$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$";

    private static final String VALID_API_KEY_REGEX
            = "^[A-Za-z0-9]{8}-[A-Za-z0-9]{8}-[A-Za-z0-9]{8}-[A-Za-z0-9]{8}$";

    @Id
    private ObjectId _id;
    private String email;
    private String api_key;

    private static boolean isValid(String email) {
        return Pattern.matches(VALID_EMAIL_ADDRESS_REGEX, email);
    }

    private static boolean isValidKey(String key) {
        return Pattern.matches(VALID_API_KEY_REGEX, key);
    }
    
    public boolean verify(Key k) {
        return isValidKey(k.getapi_key()) && isValid(k.getEmail());
    }

    public Key() {}

    public Key(String email) {
        if (isValid(email)) {
            this.email = email;
            this.api_key = keyGen();
        } else {
            this.email = "";
            this.api_key = "";
        }
    }

    public Key(String email, String key) {
        if (isValid(email) && isValidKey(key)) {
            this.email = email;
            this.api_key = key;
        }
    }

    private static String keyGen() {
        String newKey = "";
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 35; i++) {
            int num = rand.nextInt(charset.length());
            newKey += (i % 9 == 0) && (i != 0) ? "-" : charset.charAt(num);
        }
        return newKey;
    }

    public ObjectId get_id() {
        return this._id;
    }

    public void setEmail(String email) {
        if (isValid(email)) {
            this.email = email;
        }
    }

    public String getEmail() {
        return this.email;
    }

    public void setapi_key(String api_key) {
        if (isValidKey(api_key)) {
            this.api_key = api_key;
        }
    }

    public String getapi_key() {
        return this.api_key;
    }

    public org.bson.Document toJSON() {
        return new org.bson.Document()
                .append("email", this.email)
                .append("api_key", this.api_key);
    }
}
