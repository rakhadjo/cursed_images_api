/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.repositories;

import com.cursed.cursed.models.APIKey;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author rakhadjo
 */
public interface APIKeyRepo extends CrudRepository<APIKey, Integer> {
    APIKey findByKey(String key);
}
