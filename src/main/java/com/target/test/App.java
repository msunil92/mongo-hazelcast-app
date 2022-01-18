package com.target.test;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.target.entity.Store;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static java.util.Collections.singletonList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class App {

    public static void main(String[] args) {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
            MongoDatabase db = mongoClient.getDatabase("test");
            MongoCollection<ZipCode> grades = db.getCollection("stores", ZipCode.class);

            // create a new grade.
//            Grade newGrade = new Grade().setStudentId(10003d)
//                    .setClassId(10d)
//                    .setScores(singletonList(new Score().setType("homework").setScore(50d)));

            ZipCode grade = ZipCode.builder()
                    .zipcode("165481")
                    .stores(singletonList(Store.builder().storeNo("1234").distance(50).build())).build();
            grades.insertOne(grade);
            System.out.println("Grade inserted.");

            // find this grade.
//            Grade grade = grades.find(eq("student_id", 10003d)).first();
//            System.out.println("Grade found:\t" + grade);

//            // update this grade: adding an exam grade
//            List<Score> newScores = new ArrayList<>(grade.getScores());
//            newScores.add(new Score().setType("exam").setScore(42d));
//            grade.setScores(newScores);
//            Document filterByGradeId = new Document("_id", grade.getId());
//            FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
//            Grade updatedGrade = grades.findOneAndReplace(filterByGradeId, grade, returnDocAfterReplace);
//            System.out.println("Grade replaced:\t" + updatedGrade);
//
//            // delete this grade
//            System.out.println("Grade deleted:\t" + grades.deleteOne(filterByGradeId));
        }
    }
}
