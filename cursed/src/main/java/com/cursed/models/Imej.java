/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author rakhadjo
 */
@Document(collection = "images")
public class Imej {
    @Id
    private String id;
    private String url;
    
    public Imej() {}
    
    public String getUrl() { return this.url; }
    public String getId() { return this.id; }
}
