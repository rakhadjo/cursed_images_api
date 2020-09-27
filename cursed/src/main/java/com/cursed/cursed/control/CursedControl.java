/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.control;

import com.cursed.cursed.models.APIKey;
import com.cursed.cursed.models.Imej;
import com.cursed.cursed.models.Response;
import com.cursed.cursed.models.Result;
import com.cursed.cursed.repositories.APIKeyRepo;
import com.cursed.cursed.repositories.ImejRepo;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public String xTraceGen() {
        String newTrace = "";
        String charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 12; i++) {
            int num = rand.nextInt(charSet.length());
            newTrace += charSet.charAt(num);
        }
        return newTrace;
    }

    @GetMapping("/all")
    public List<Imej> getAll() {
        return imejRepo.findAll();
    }

    /**
     *
     * @return
     */
    @GetMapping("/test")
    public ResponseEntity getTest() {
        Response r = new Response();
        try {
            List<Imej> list = imejRepo.findAll();
            r.setCodes(Result.SUCCESS);
            r.setImejs(list);
        } catch (Exception e) {
            r.setCodes(Result.FAIL_GET_IMAGE);
        }
        return new ResponseEntity(r.toJSON(), null, HttpStatus.OK);
    }

    /**
     * @param api_token
     * @return
     */
    @GetMapping("/get")
    public ResponseEntity getRandom(
            @RequestHeader("key") String api_token) {
        Response r;
        if (bucket.tryConsume(1)) {
            try {
                APIKey apikey = apiKeyRepo.findByToken(api_token);
                if (apikey != null) {
                    if (apikey.getKey().equals(api_token)) {
                        int num = rand.nextInt((int) imejRepo.count());
                        r = new Response(Result.SUCCESS);
                        r.setImej(imejRepo.findBy_id(num));
                        return new ResponseEntity(r.toJSON(), null, HttpStatus.OK);
                    } else {
                        r = new Response(Result.FAIL_KEY_VERIFICATION);
                        return new ResponseEntity(r.toJSON(), null, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    r = new Response(Result.FAIL_KEY_VERIFICATION);
                    return new ResponseEntity(r.toJSON(), null, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                r = new Response(Result.FAIL);
                r.setMessage(e.getLocalizedMessage());
                e.printStackTrace();
                return new ResponseEntity(r.toJSON(), null, HttpStatus.BAD_REQUEST);
            }
        } else {
            r = new Response(Result.TOO_MANY_REQUESTS);
            return new ResponseEntity(r.toJSON(), null, HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    /**
     *
     * @param i
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity<Document> saveImej(
            @RequestBody Imej i) {
        Response r;
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-trace-id", xTraceGen());
        String url = i.getUrl();
        if (imejRepo.findByUrl(url) != null) {
            r = new Response(Result.IMAGE_EXISTS);
            return new ResponseEntity(r.toJSON(), headers, HttpStatus.CONFLICT);
        }
        try {
            imejRepo.save(new Imej(url));
            r = new Response(Result.SUCCESS);
            r.setImej(i);
            return new ResponseEntity(r.toJSON(), headers, HttpStatus.OK);
        } catch (Exception e) {
            r = new Response(Result.FAIL);
            r.setMessage(e.getMessage());
            return new ResponseEntity(r.toJSON(), headers, HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @GetMapping("/sql")
    public @ResponseBody
    String test2SQL(
            @RequestHeader Map<String, String> headers) {
        try {
            String key = headers.get("api_token");
            APIKey key2 = apiKeyRepo.findByToken(key);
            return key2.getKey();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/allkeys")
    public @ResponseBody
    Iterable<APIKey> allKeys() {
        try {
            return apiKeyRepo.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;

    }

    @GetMapping("/teapot")
    public ResponseEntity<Document> teapot() {
        Document teapot = new Document()
                .append("I'm a little teapot", "Short and stout")
                .append("Here is my handle", "Here is my spout")
                .append("When I get all steamed up", "Hear me shout")
                .append("Tip me over and", "Pour me out!")
                .append("", "")
                .append("I'm a very special teapot", "Yes, it's true")
                .append("Here's an example of what I can do", "I can turn my handle into a spout")
                .append("Tip me over and2", "Pour me out!2");
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-trace-id", xTraceGen());
        return new ResponseEntity(teapot, headers, HttpStatus.I_AM_A_TEAPOT);
    }

}
