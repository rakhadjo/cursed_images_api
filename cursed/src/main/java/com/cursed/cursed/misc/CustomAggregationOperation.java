/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cursed.cursed.misc;

import com.mongodb.DBObject;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

/**
 * @resource
 * https://stackoverflow.com/questions/59697496/how-to-do-a-mongo-aggregation-query-in-spring-data
 * @author rakhadjo
 */
public class CustomAggregationOperation implements AggregationOperation {
    
    private DBObject operation;

    public CustomAggregationOperation (DBObject operation) {
        this.operation = operation;
    }

    @Override
    public DBObject toDBObject(AggregationOperationContext context) {
        return context.getMappedObject(operation);
    }

}
