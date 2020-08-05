/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.control;

import com.cursed.cursed.misc.Key;
import com.cursed.cursed.models.Imej;
import com.cursed.cursed.models.Response;
import com.cursed.cursed.models.ResponseResult;
import com.cursed.cursed.models.Result;
import com.cursed.cursed.repositories.ImejRepo;
import com.cursed.cursed.repositories.KeyRepo;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rakhadjo
 */
@RestController
@RequestMapping("/api")
public class CursedControl {

    @Autowired
    private ImejRepo imejRepo;
    @Autowired
    private KeyRepo keyRepo;
    private Random rand = new Random();

    @GetMapping("/testkey")
    public Document testKey(@RequestHeader Map<String, String> headers) {
        try {
            Document d = new Document();
            Key k = keyRepo.findByEmail(headers.get("email"));
            boolean matches = k.getapi_key().equals(headers.get("api_key"));
            d.append("keys match", matches);
            return d;
        } catch (Exception e) {
            return new Document("error", e.getMessage());
        }

    }

    @GetMapping("/test")
    public Document getTest() {
        Response r = new Response();
        try {
            List<Imej> list = imejRepo.findAll();
            r.setCodes(Result.SUCCESS);
            r.setImejs(list);
        } catch (Exception e) {
            r.setCodes(Result.FAIL_GET_IMAGE);
        }
        return r.toJSON();
    }

    @GetMapping("/get")
    public @ResponseBody Document getRandom(@RequestHeader Map<String, String> headers) {
//        //int num = rand.nextInt((int) imejRepo.count());
//        Response r = new Response(Result.SUCCESS);
//        //r.setImej(repo.findByNum(num));
//        return r.toJSON();
        Response r;
        try {
            Key k = new Key(headers.get("email"), headers.get("api_key"));
        } catch(Exception e) {
            
        }
        r = new Response();
        return r.toJSON();
    }

    @GetMapping("/get2")
    public Document random2() {

        return new Document();
    }

    @PostMapping("/register")
    public @ResponseBody Document addKey(@RequestHeader Map<String, String> headers) {
        Response response;
        try {
            Key check = keyRepo.findByEmail(headers.get("email"));
            if (check == null) {
                Key k = new Key(headers.get("email"));
                keyRepo.save(k);
                response = new Response(Result.SUCCESS);
                response.setKey(k);
                return response.toJSON();
            } else {
                return new Response(Result.KEY_REGISTER_FAIL).toJSON();
            }
        } catch (Exception e) {
            response = new Response(Result.FAIL);
            response.setMessage(e.getMessage());
            return new Document("error", e.getMessage());
        }
    }

    //bad practice, never do this!
    @GetMapping("/allkeys")
    public List<Key> getKeys() {
        return keyRepo.findAll();
    }

}
