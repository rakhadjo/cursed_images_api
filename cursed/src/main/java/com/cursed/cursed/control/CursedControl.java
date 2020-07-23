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
import com.cursed.cursed.repositories.ResponsesRepo;
import java.util.List;
import java.util.Random;
import javax.validation.Valid;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author rakhadjo
 */
@RestController
@RequestMapping("/api")
public class CursedControl {

    @Autowired
    private ImejRepo repo;
    @Autowired
    private ResponsesRepo resp_repo;
    private Random rand = new Random();

    @GetMapping("/test")
    public Document getTest() {
        Response r = new Response();
        try {
            List<Imej> list = repo.findAll();
            r.setCodes(Result.SUCCESS);
            r.setImejs(list);
        } catch (Exception e) {
            r.setCodes(Result.FAIL_ALL);
        }
        return r.toJSON();
    }

    @GetMapping("/get")
    public Document getRandom(@RequestHeader Key key) {
        Response r;
        
        int num = rand.nextInt((int) repo.count());
        r = new Response(Result.SUCCESS);
        r.setImej(repo.findByNum(num));
        return r.toJSON();
    }

    /*
    EXPERIMENTAL RANDOM GET
    */
    @GetMapping("/get2")
    public Response getRandom2() {
        Response r = new Response(Result.SUCCESS);
        resp_repo.save(r);
        return r;
    }

    @PostMapping("/save")
    public Document saveImej(@Valid @RequestBody Imej imej) {
        if (imej.getNum() > (int) repo.count() && repo.findByNum(imej.getNum()) == null) {
            repo.save(imej);
            return new Response(Result.SUCCESS).toJSON();
        }
        return new Response(Result.EXISTS).toJSON();

    }

}
