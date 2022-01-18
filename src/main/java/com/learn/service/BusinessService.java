package com.learn.service;

import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.learn.entity.Store;
import com.learn.entity.StoreSave;
import com.learn.entity.ZipcodeInfo;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.push;

/**
 * @author sunil
 * @project mongo-app
 * @created 2022/01/06 6:50 PM
 */

@Service
public class BusinessService {

    @Autowired
    MongoDatabase mongoDatabase;

    public void process() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("customer");
        FindIterable<Document> iterable = collection.find();
        MongoCursor<Document> cursor = iterable.iterator();
        System.out.println("customer list with cursor: ");
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

    public void save1(StoreSave storeSave) {
        MongoCollection<ZipcodeInfo> zipCodeMongoCollection = mongoDatabase.getCollection("stores", ZipcodeInfo.class);

        long count = zipCodeMongoCollection
                .countDocuments(and(
                        eq("destZipCode" , storeSave.getZipcode()),
                        eq("Stores.storeNo" , storeSave.getStoreNo())
                ));

        if (count == 0) {

            Store store = Store.builder().storeNo(storeSave.getStoreNo()).distance(storeSave.getDistance()).build();
            ZipcodeInfo zipCode = ZipcodeInfo.builder()
                    .zipCode(storeSave.getZipcode())
                    .storeList(Arrays.asList(store)).build();

            zipCodeMongoCollection.insertOne(zipCode);
            System.out.println("Grade inserted.");
        } else if (count == 1) {
            return;
        } else {
            Store newStore = Store.builder().storeNo(storeSave.getStoreNo()).distance(storeSave.getDistance()).build();
            UpdateOptions options = new UpdateOptions().upsert(true);
            Bson filter = eq("destZipCode", storeSave.getZipcode());
            Bson updateOperation = push("Stores", newStore);
            UpdateResult updateResult = zipCodeMongoCollection.updateOne(filter, updateOperation, options);
            System.out.println(updateResult);
        }
    }

    public void save(StoreSave storeSave, Map<String,ZipcodeInfo> hm ) {
        MongoCollection<ZipcodeInfo> zipCodeMongoCollection = mongoDatabase.getCollection("stores3", ZipcodeInfo.class);

//        IndexOptions indexOptions = new IndexOptions().unique(true);
//        String resultCreateIndex = zipCodeMongoCollection.createIndex(Indexes.descending("destZipCode"), indexOptions);



        Store store = Store.builder().storeNo(storeSave.getStoreNo()).distance(storeSave.getDistance()).build();
        ZipcodeInfo zipCodeInfo = ZipcodeInfo.builder()
                .zipCode(storeSave.getZipcode())
                .storeList(Arrays.asList(store)).build();


        try {
            zipCodeMongoCollection.insertOne(zipCodeInfo);
            updateCache(storeSave, hm, zipCodeMongoCollection);
            System.out.println("ZipInfo inserted.");
            return;
        } catch (Exception e) {
            System.out.println("Duplicate records");
        }

        long count = zipCodeMongoCollection
                .countDocuments(and(
                        eq("destZipCode" , storeSave.getZipcode()),
                        eq("Stores.storeNo" , storeSave.getStoreNo())
                ));


        if ( count ==0) {
            Store newStore = Store.builder().storeNo(storeSave.getStoreNo()).distance(storeSave.getDistance()).build();
            UpdateOptions options = new UpdateOptions().upsert(true);
            Bson filter = eq("destZipCode", storeSave.getZipcode());
            Bson updateOperation = push("Stores", newStore);
            UpdateResult updateResult = zipCodeMongoCollection.updateOne(filter, updateOperation, options);
            System.out.println(updateResult);
        }
        updateCache(storeSave, hm, zipCodeMongoCollection);

    }

    public void updateCache(StoreSave storeSave, Map<String,ZipcodeInfo> hm, MongoCollection<ZipcodeInfo> zipCodeMongoCollection) {
        ZipcodeInfo updatedZipcodeInfo = zipCodeMongoCollection.find(and(
                eq("destZipCode" , storeSave.getZipcode()),
                eq("Stores.storeNo" , storeSave.getStoreNo())
        )).first();
        hm.put(storeSave.getZipcode(), updatedZipcodeInfo);
    }
}

//db.stores.createIndex( { "destZipCode": 1 }, { unique: true, sparse: true, expireAfterSeconds: 3600 } )

//  db.stores.find( { "stores": {  storeNo: "" } } )