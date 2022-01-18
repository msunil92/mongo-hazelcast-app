package com.learn.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * @author sunil
 * @project mongo-app
 * @created 2022/01/09 10:48 AM
 */

@Configuration
public class MongoConfiguration {

    @Value("${mongodb.uri}")
    String mongodbUri;

    @Value("${mongodb.database}")
    String mongoDB;

    @Bean
    MongoDatabase establishMongoDBConnection() {
        ConnectionString connectionString = new ConnectionString(mongodbUri);
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoCredential credential = MongoCredential.createCredential("mongo", mongoDB, "mongo".toCharArray());

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                //.credential(credential)
                .codecRegistry(codecRegistry)
                .build();

        MongoClient mongoClient = MongoClients.create(clientSettings);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(mongoDB);
        return mongoDatabase;


    }
}
