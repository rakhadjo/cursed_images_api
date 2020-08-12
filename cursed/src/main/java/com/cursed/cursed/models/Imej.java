/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.models;

import com.cursed.cursed.misc.BadImejException;
import java.net.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author rakhadjo
 */
@Document(collection = "imej")
public class Imej {

    @Id
    private int _id;
    private String url;

    /**
     *
     */
    public static int count;

    /**
     * Empty Constructor
     */
    public Imej() {
    }

    /**
     * Constructor with URL in String format
     * @param url
     * @throws com.cursed.cursed.misc.BadImejException
     */
    public Imej(String url) throws BadImejException {
        if (isValidURL(url)) {
            this._id = count++;
            this.url = url;
        }
    }
    
    /**
     * 
     * @param newCount 
     */
    public static void setCount(int newCount) {
        count = newCount;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return this.url;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return this._id;
    }
    
    /**
     * 
     * @param u
     * @return 
     * @throws com.cursed.cursed.misc.BadImejException 
     */
    public boolean isValidURL(String u) throws BadImejException {
        try {
            new URL(u).toURI();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadImejException("Bad URL Format. Try again!");
        }
        
        
    }

    /**
     *
     * @return
     */
    public org.bson.Document toJSON() {
        return new org.bson.Document()
                .append("url", this.url);
    }
}
