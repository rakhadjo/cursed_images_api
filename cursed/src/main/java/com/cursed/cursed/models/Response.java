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
    private String rc;
    //  Message
    private String message;
    //  Imej object
    private Imej imej;
    //  Imej List
    private List<Imej> imejs;
    
    public Response() {}
    
    public Response(Result m) {
        setCodes(m);
    }
    
    public final void setCodes(Result m) {
        switch (m) {
            case SUCCESS:
                this.rc = "00";
                this.message = "Success";
                break;
            case EXISTS:
                this.rc = "10";
                this.message = "Already exists";
                break;
            case FAIL_ALL:
                this.rc = "11";
                this.message = "Failed to retrieve all";
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
    
    public org.bson.Document toJSON() {
        org.bson.Document doc = new org.bson.Document()
                .append("rc", this.rc)
                .append("message", this.message);
        
        if (!this.imej.equals(null)) {
            doc.append("imej", this.imej);
        }
        else if (!this.imejs.isEmpty()) {
            doc.append("imejs", this.imejs);
        }
        return doc;
        
    }
    
}
