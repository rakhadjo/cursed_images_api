/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.models;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;



/**
 *
 * @author rakhadjo
 */
@Document(collection = "responses")
public class Response {
    
    //  Response Code
    private String rc = null;
    //  Message
    private String message = null;
    //  Imej object
    private Imej imej = null;
    //  Imej List
    private List<Imej> imejs = null;
    //  API Key used
    private String api_key = null;
    
    public Response() {}
    
    public Response(ResponseResult m) {
        switch (m) {
            case SUCCESS:
                this.rc = "00";
                this.message = "Success";
                break;
            case KEY_FAIL:
                this.rc = "01";
                this.message = "Invalid API Key";
                break;
            case EXISTS:
                this.rc = "10";
                this.message = "Already exists";
                break;
            case FAIL:
                this.rc = "11";
                this.message = "Failed to retrieve";
                break;
            case FAIL_ALL:
                this.rc = "12";
                this.message = "Failed to retrieve all";
                break;
            default:
                this.rc = "99";
                this.message = "Undefined Error";
                break;
        }
    }
    
    public void setImej(Imej i) {
        this.imej = i;
    }
    
    public void setImejs(List<Imej> l) {
        this.imejs = l;
    }
    
    public void setKey(String api_key) {
        this.api_key = api_key;
    }
    
    public org.bson.Document toJSON() {
        org.bson.Document doc = new org.bson.Document()
                .append("rc", this.rc)
                .append("message", this.message);
        
        try {
            doc.append("imej", this.imej);
        } catch (Exception e) {
            doc.append("Error", e.getMessage());
        }
        doc.append("imejs", this.imejs);
        return doc;
        
    }
    
}
