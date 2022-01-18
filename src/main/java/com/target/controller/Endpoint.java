package com.target.controller;

import com.hazelcast.core.HazelcastInstance;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.target.entity.Store;
import com.target.entity.StoreSave;
import com.target.entity.ZipcodeInfo;
import com.target.service.BusinessService;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author sunil
 * @project mongo-app
 * @created 2022/01/06 6:47 PM
 */
@RestController
public class Endpoint {

    @Autowired
    BusinessService businessService;

    @Autowired
    MongoDatabase mongoDatabase;

    @Autowired
    HazelcastInstance hazelcastInstance;

    @PostConstruct
    void  init () {
        MongoCollection<ZipcodeInfo> zipCodeMongoCollection = mongoDatabase.getCollection("stores3", ZipcodeInfo.class);

//        mongoDatabase.getCollection("stores3").drop();

        IndexOptions indexOptions = new IndexOptions().unique(true);
        String resultCreateIndex = zipCodeMongoCollection.createIndex(Indexes.descending("destZipCode"), indexOptions);
        System.out.println(resultCreateIndex);

        Map<String,ZipcodeInfo> hm = hazelcastInstance.getMap("stores");

        MongoCollection<ZipcodeInfo> document = mongoDatabase.getCollection("stores3", ZipcodeInfo.class);

        MongoCursor<ZipcodeInfo> cursor = document.find().iterator();
        while (cursor.hasNext()) {
            ZipcodeInfo zipcodeInfo = cursor.next();
            hm.put(zipcodeInfo.getZipCode(),  zipcodeInfo);
        }
    }

    @GetMapping("/status")
    public String getStatus() {
        return "Working";
    }

    @GetMapping("/test")
    public String test() {
        Map<String,String> hm = hazelcastInstance.getMap("test");
        System.out.println(hm);
        System.out.println(hm.keySet());
        System.out.println(hm.entrySet());
        System.out.println(hm.get("sunil"));
        //businessService.process();
        return "Done";
    }

    @PostMapping(value = "insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveRecord(@RequestBody StoreSave storeSave) {

        String response = "";
        System.out.println("Request: " + storeSave);
        String zipCode  = storeSave.getZipcode();
        String storeNo = storeSave.getStoreNo();

        Map<String,ZipcodeInfo> hm = hazelcastInstance.getMap("stores");
        ZipcodeInfo zipcodeInfo = hm.get(zipCode);


        if (cacheSearch(storeNo, zipcodeInfo)) {
            response = response + "Fetched from cache: " + hm.get(zipCode);
        }else {
            response = response +"From mongo db and updated in cache";
            businessService.save(storeSave, hm );
        }

        return response;
    }

    boolean cacheSearch( String store, ZipcodeInfo zipcodeInfo) {
        if (null == zipcodeInfo) return false;

        return zipcodeInfo.getStoreList()
                .stream()
                .filter( cache -> cache.getStoreNo().equalsIgnoreCase(store))
                .findFirst().isPresent();

    }


}
