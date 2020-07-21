/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.control;

import com.cursed.cursed.repositories.ImejRepo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    
    @GetMapping("/test")
    public Document getTest() {
        return new Document()
                .append("rc", "00")
                .append("code", repo.findAll());
    }
    
}
