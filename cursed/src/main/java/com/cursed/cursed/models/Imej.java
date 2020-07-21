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
    
    public Imej() {}
    
    public String getUrl() { return this.url; }
    public ObjectId getId() { return this._id; }
}
