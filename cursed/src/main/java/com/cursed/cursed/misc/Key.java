/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.misc;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author rakhadjo
 */
@Document(collection = "api_keys")
public class Key {
    
    @Id
    private ObjectId _id;
    private String api_key;
    
    public Key() {}
    
    public void set_id(ObjectId _id) {
        this._id = _id;
    }
    public ObjectId get_id() {
        return this._id;
    }
    
    public void setapi_key(String api_key) {
        this.api_key = api_key;
    }
    public String getapi_key() {
        return this.api_key;
    }
}
