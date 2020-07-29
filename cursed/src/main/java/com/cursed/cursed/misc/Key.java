/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.misc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author rakhadjo
 */
//@Document(collection = "api_keys")
@Entity
@Table(name = "api_keys")
public class Key {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String api_key;
    
    public Key() {}
    
    public Key(String key) {
        this.api_key = key;
    }
    
    public void set_id(Integer id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
    
    public void setapi_key(String api_key) {
        this.api_key = api_key;
    }
    public String getapi_key() {
        return this.api_key;
    }
}
