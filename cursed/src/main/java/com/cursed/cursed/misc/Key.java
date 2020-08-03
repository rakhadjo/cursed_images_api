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

/**
 *
 * @author rakhadjo
 */
@Document(collection = "api_keys")
public class Key {
    
    private static Random rand = new Random();
    
    @Id
    private ObjectId _id;
    private String email;
    private String api_key;
    
    public Key() {}
    
    public Key(String email) {
        this.email = email;
        this.api_key = keyGen();
    }
    
    public Key(String email, String key) {
        this.email = email;
        this.api_key = key;
    }
    
    private static String keyGen() {
        String newKey = "";
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 36; i++) {
            int num = rand.nextInt(charset.length());
            newKey += (i % 9 == 0) && (i != 0) ? "-" : charset.charAt(num);
        }
        return newKey;
    }
    
    public void set_id(ObjectId id) {
        this._id = id;
    }
    public ObjectId get_id() {
        return this._id;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setapi_key(String api_key) {
        this.api_key = api_key;
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
