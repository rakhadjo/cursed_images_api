/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.repositories;

import com.cursed.cursed.models.Imej;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author rakhadjo
 */
@Repository
public interface ImejRepo extends MongoRepository<Imej, String>{
    
    
    
}
