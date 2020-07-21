/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.repositories;

import com.cursed.models.Imej;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author rakhadjo
 */
@RepositoryRestResource(collectionResourceRel = "imejs", path = "imejs") 
public interface ImejRepo extends MongoRepository<Imej, String>{
    
    Imej findById();
    
    List<Imej> findAll();
    
}
