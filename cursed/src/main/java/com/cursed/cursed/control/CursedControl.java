/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.control;

import com.cursed.cursed.misc.InvalidAPIKeyException;
import com.cursed.cursed.misc.Key;
import com.cursed.cursed.models.Imej;
import com.cursed.cursed.models.Response;
import com.cursed.cursed.models.ResponseResult;
import com.cursed.cursed.repositories.ImejRepo;
import com.cursed.cursed.repositories.KeyRepo;
import com.cursed.cursed.repositories.ResponsesRepo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/cursed_database";
    
    private boolean verify(String key) {
        try {
            Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM api_keys;";
            ResultSet rslt = stmt.executeQuery(sql);
            while (rslt.next()) {
                if (rslt.getString("api_key").equals(key)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            
        } return false; 
    }

    @Autowired
    private ImejRepo repo;
    @Autowired
    private ResponsesRepo resp_repo;
    @Autowired
    private KeyRepo key_repo;
    
    private boolean verify2(String key) {
        return key_repo.findKey(key) == null;
    }
    private Random rand = new Random();

    @GetMapping("/test")
    public Document getTest() {
        Response r;
        try {
            List<Imej> list = repo.findAll();
            r = new Response(ResponseResult.SUCCESS);
            r.setImejs(list);
        } catch (Exception e) {
            r = new Response(ResponseResult.FAIL_ALL);
        }
        return r.toJSON();
    }

    @GetMapping("/get")
    public Document getRandom(@RequestHeader String key) {
        Response r;
        if (verify(key)) {
            try {
                int num = rand.nextInt((int) repo.count());
                r = new Response(ResponseResult.SUCCESS);
                r.setImej(repo.findByNum(num));
            } catch (Exception e) {
                r = new Response(ResponseResult.FAIL_ALL);
            }
        } else {
            r = new Response(ResponseResult.KEY_FAIL);
        }
        r.setKey(key);
        return r.toJSON();
    }

    /*
    EXPERIMENTAL RANDOM GET
    */
    @GetMapping("/get2")
    public Response getRandom2(@RequestHeader String key) {
        Response r = verify(key) ? 
                new Response(ResponseResult.SUCCESS) : 
                new Response(ResponseResult.KEY_FAIL);
        resp_repo.save(r);
        return r;
    }

    @PostMapping("/save")
    public Document saveImej(@Valid @RequestBody Imej imej) {
        if (imej.getNum() > (int) repo.count() && repo.findByNum(imej.getNum()) == null) {
            repo.save(imej);
            return new Response(ResponseResult.SUCCESS).toJSON();
        }
        return new Response(ResponseResult.EXISTS).toJSON();

    }

}
