/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.control;

import com.cursed.cursed.misc.Key;
import com.cursed.cursed.models.APIKey;
import com.cursed.cursed.models.Imej;
import com.cursed.cursed.models.Response;
import com.cursed.cursed.models.Result;
import com.cursed.cursed.repositories.APIKeyRepo;
import com.cursed.cursed.repositories.ImejRepo;
import com.cursed.cursed.repositories.KeyRepo;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import java.time.Duration;
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
    @Autowired
    private APIKeyRepo apiKeyRepo;
    private Random rand = new Random();

    private final Bucket bucket;

    /**
     *
     */
    public CursedControl() {
        //20 requests maximum
        //20 keys per refill
        //refilled every one minute
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
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
     * @param headers
     * @return
     */
    @GetMapping("/get")
    public @ResponseBody
    Document getRandom(@RequestHeader Map<String, String> headers) {
        Response r;
        if (bucket.tryConsume(1)) {
            try {
//                Key k = keyRepo.findByEmail(headers.get("email"));
//                if (k.getapi_key().equals(headers.get("api_key"))) {
//                    int num = rand.nextInt((int) imejRepo.count());
//                    r = new Response(Result.SUCCESS);
//                    r.setImej(imejRepo.findBy_id(num));
//                } else {
//                    r = new Response(Result.FAIL_EMAIL_KEY_VERIFICATION);
//                }
                APIKey apikey = apiKeyRepo.findByKey(headers.get("api_token"));
                if (apikey.getKey().equals(headers.get("api_token"))) {
                    //int num = rand.nextInt((int) imejRepo.count());
                    r = new Response(Result.SUCCESS);
                    r.setImej(imejRepo.findBy_id(2));
                } else {
                    r = new Response(Result.FAIL_KEY_VERIFICATION);
                }
            } catch (Exception e) {
                r = new Response(Result.FAIL);
                r.setMessage(e.getMessage());
                e.printStackTrace();
            }
        } else {
            r = new Response(Result.TOO_MANY_REQUESTS);
        }

        return r.toJSON();
    }

    /**
     *
     * @param headers
     * @return
     */
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

    /**
     *
     * @param i
     * @return
     */
    @PostMapping("/save")
    public @ResponseBody
    Document saveImej(@RequestBody Imej i) {
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
    
    @GetMapping("/sql2")
    public @ResponseBody
    String test2SQL(@RequestHeader Map<String, String> headers) {
        try {
            String key = headers.get("api_token");
            APIKey key2 = apiKeyRepo.findByKey(key);
            return key2.getKey();
        } catch (Exception e) {
            return e.getMessage();
        }
        
    }

    @GetMapping("/sql")
    public @ResponseBody
    Iterable<APIKey> testSQL() {

        try {
            //apiKeyRepo.deleteAll();
            apiKeyRepo.save(new APIKey());
            return apiKeyRepo.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } return null;

    }

}