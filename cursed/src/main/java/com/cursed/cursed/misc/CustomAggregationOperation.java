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

//    private String jsonOperation;
//
//    public CustomAggregationOperation(String jsonOperation) {
//        this.jsonOperation = jsonOperation;
//    }
//
//    @Override
//    public Document toDocument(AggregationOperationContext aggregationOperationContext) {
//        return aggregationOperationContext.getMappedObject(org.bson.Document.parse(jsonOperation));
//    }
    
    private DBObject operation;

    public CustomAggregationOperation (DBObject operation) {
        this.operation = operation;
    }

    //@Override
    public DBObject toDBObject(AggregationOperationContext context) {
        return (DBObject) context.getMappedObject((Document) operation);
    }

    @Override
    public Document toDocument(AggregationOperationContext aoc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
