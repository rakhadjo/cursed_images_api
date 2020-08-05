/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.models;

import com.cursed.cursed.misc.Key;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author rakhadjo
 */
public class Response {
    
    //  Response Code
    private String rc;
    //  Message
    private String message;
    //  Imej object
    private Imej imej;
    //  Imej List
    private List<Imej> imejs;
    //  Key Object
    private Key key;
    
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
            case IMAGE_EXISTS:
                this.rc = "10";
                this.message = "Image already exists";
                break;
            case FAIL_GET_IMAGE:
                this.rc = "11";
                this.message = "Failed to retrieve image";
                break;
            case KEY_REGISTER_FAIL:
                this.rc = "12";
                this.message = "Key / Email registration failed";
                break;
            case FAIL_EMAIL_KEY_VERIFICATION:
                this.rc = "13";
                this.message = "Email and API Key Invalid";
                break;
            case FAIL_EMAIL_VERIFICATION:
                this.rc = "13a";
                this.message = "Email Invalid";
                break;
            case FAIL_KEY_VERIFICATION:
                this.rc = "13b";
                this.message = "API Key Invalid";
                break;
            case FAIL:
                this.rc = "98";
                this.message = "Misc. Fail";
                break;
            default:
                this.rc = "-";
                this.message = "Undefined Error";
                break;
        }
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setImej(Imej i) {
        this.imej = i;
    }
    
    public void setImejs(List<Imej> l) {
        this.imejs = l;
    }
    
    public void setKey(Key key) {
        this.key = key;
    }
    
    public Document toJSON() {
        Document doc = new Document()
                .append("rc", this.rc)
                .append("message", this.message);
        
        if (this.imej != null) {
            doc.append("imej", this.imej.toJSON());
        }
        if (this.imejs != null) {
            if (!this.imejs.isEmpty()) {
                doc.append("imejs", this.imejs);
                }
        }
        if (this.key != null) {
            doc.append("key", this.key.toJSON());
        }
        return doc;
        
    }
    
}
