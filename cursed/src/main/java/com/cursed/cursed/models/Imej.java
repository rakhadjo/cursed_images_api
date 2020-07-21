/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author rakhadjo
 */
@Document(collection = "imej")
public class Imej {
    @Id
    private ObjectId _id;
    private String url;
    private int num;
    
    public Imej() {}
    
    public String getUrl() { return this.url; }
    public ObjectId getId() { return this._id; }
    public int getNum() { return this.num; }
    
    public org.bson.Document toJSON() {
        return new org.bson.Document()
                .append("_id", this._id)
                .append("num", this.num)
                .append("url", this.url);
    }
}
