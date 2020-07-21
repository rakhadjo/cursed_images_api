/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.control;

import com.cursed.cursed.models.Imej;
import com.cursed.cursed.repositories.ImejRepo;
import java.util.Random;
import javax.validation.Valid;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    //private Document entries = new Document("code", repo.findAll());
    private Random rand = new Random();
    
    @GetMapping("/test")
    public Document getTest() {
        return new Document()
                .append("rc", "00")
                .append("code", repo.findAll());
    }
    
    @GetMapping("/get")
    public Document getRandom() {
        int num = rand.nextInt((int)repo.count());
        return repo.findByNum(num).toJSON();
    }
    
    @PostMapping("/save")
    public Document saveImej(@Valid @RequestBody Imej imej) {
        if (imej.getNum() > (int)repo.count() && repo.findByNum(imej.getNum()) == null) {
            repo.save(imej);
            return new Document().append("rc", "00");
        }
        return new Document()
                .append("rc", "10")
                .append("message", "num already exists");
    }
    
}
