/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.control;

import com.cursed.cursed.misc.Key;
import com.cursed.cursed.models.Imej;
import com.cursed.cursed.models.Response;
import com.cursed.cursed.models.Result;
import com.cursed.cursed.repositories.ImejRepo;
import com.cursed.cursed.repositories.KeyRepo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    /**
     * 
     * @return 
     */
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
    /**
     * 
     * @return 
     */
    @GetMapping("/get")
    public @ResponseBody
    Document getRandom() {
        Response r;
        int num = rand.nextInt((int) imejRepo.count());
        r = new Response(Result.SUCCESS);
        r.setImej(imejRepo.findBy_id(num));
        return r.toJSON();
    }
    /**
     * 
     * @param headers
     * @return 
     */
    @GetMapping("/get2")
    public Document random2(@RequestHeader Map<String, String> headers) {
        // Takes the number of requested images as a header "num";
        int numImages = Integer.parseInt(headers.get("num"));
        if (numImages < 2) {
            return getRandom();
        }
        if (numImages > 100) {
            return getNRandom(100);
        }
        return getNRandom(numImages);
    }

    public Document getNRandom(int n) {
        int maxImages = (int) imejRepo.count();

        if (n > maxImages) {
            return getNRandom(maxImages);
        }

        Response r = new Response(Result.SUCCESS);

        HashSet<Integer> previousNums = new HashSet<Integer>();
        ArrayList<Imej> images = new ArrayList<Imej>();
        int num = rand.nextInt((int) imejRepo.count());

        while (n > 0) {
            while (previousNums.contains(num)) {
                num = rand.nextInt((int) imejRepo.count());
            }
            previousNums.add(num);
            images.add(imejRepo.findBy_id(num));
            n--;
        }

        r.setImejs(images);
        return r.toJSON();
    }

    @PostMapping("/register")
    public @ResponseBody
    Document addKey(@RequestHeader Map<String, String> headers) {
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

    @PostMapping("/save")
    public @ResponseBody Document saveImej(@RequestBody Imej i) {
        Response r;
        String url = i.getUrl();
        if (imejRepo.findByUrl(url) != null) {
            r = new Response(Result.IMAGE_EXISTS);
            return r.toJSON();
        }
        try {
            imejRepo.save(new Imej(url));
            r = new Response(Result.SUCCESS);
            r.setImej(i);
        } catch (Exception e) {
            r = new Response(Result.FAIL);
            r.setMessage(e.getMessage());
        }
        return r.toJSON();
    }

}
