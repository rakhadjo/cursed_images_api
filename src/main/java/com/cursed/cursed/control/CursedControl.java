/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.control;

import com.cursed.cursed.models.*;
import com.cursed.cursed.repositories.*;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.List;
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
    @Autowired
    private KeyRepo keyRepo;

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
    public String xTraceGen() {
        String newTrace = "";
        String charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 12; i++) {
            int num = rand.nextInt(charSet.length());
            newTrace += charSet.charAt(num);
        }
        return newTrace;
    }
    
    //from https://www.geeksforgeeks.org/md5-hash-in-java/
    /**
     * 
     * @param input
     * @return 
     */
    public static String getMD5(String input) {
        try { 
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes()); 
            BigInteger no = new BigInteger(1, messageDigest); 
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } 
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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
            r.setMessage(e.getMessage());
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
        HttpStatus status;
        if (bucket.tryConsume(1)) {
            try {
                Key apikey = keyRepo.findByToken(api_token);
                if (apikey != null) {
                    if (apikey.getapi_key().equals(api_token)) {
                        int num = rand.nextInt((int) imejRepo.count());
                        r = new Response(Result.SUCCESS);
                        r.setImej(imejRepo.findBy_id(num));
                        status = HttpStatus.OK;
                    } else {
                        r = new Response(Result.FAIL_KEY_VERIFICATION);
                        status = HttpStatus.FAILED_DEPENDENCY;
                    }
                } else {
                    r = new Response(Result.FAIL_KEY_VERIFICATION);
                    status = HttpStatus.HTTP_VERSION_NOT_SUPPORTED;
                }
            } catch (Exception e) {
                r = new Response(Result.FAIL);
                r.setMessage(e.getLocalizedMessage());
                e.printStackTrace();
                status = HttpStatus.BAD_REQUEST;
            }
        } else {
            r = new Response(Result.TOO_MANY_REQUESTS);
            status = HttpStatus.TOO_MANY_REQUESTS;
        }
        return new ResponseEntity(r.toJSON(), null, status);
    }

    @PostMapping("/boss")
    public ResponseEntity bossSave(
            @RequestHeader("bosskey") String bosskey,
                @RequestBody Imej[] i) {
        Response r;
        try {
        if (getMD5(bosskey).equals("e25aa11713ee13513afbc874040ef1de")) {
            for (Imej imej : i) {
                Imej k = new Imej(imej.getUrl());
                imejRepo.save(k);
            }
            r = new Response(Result.SUCCESS);
            return new ResponseEntity(r.toJSON(), null, HttpStatus.ACCEPTED);
        } else {
            r = new Response(Result.FAIL);
            r.setMessage("bosskey invalid");
            return new ResponseEntity(r.toJSON(), null, HttpStatus.BAD_REQUEST);
        }} catch (Exception e) {}
        return new ResponseEntity(new Response(Result.FAIL).toJSON(), null, HttpStatus.I_AM_A_TEAPOT);
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

    /**
     * 
     * @return 
     */
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
