package playwright;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class MongoDBConnection {

    private static final Logger log = LoggerFactory.getLogger(MongoDBConnection.class);
    private static final String CONNECTION_URI = "mongodb://localhost:27017";

    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBConnection(String databasename) {
        try {
            mongoClient = MongoClients.create(CONNECTION_URI);
            database = mongoClient.getDatabase(databasename);
            log.info("Connected to MongoDB: {}", databasename);
        } catch (Exception e) {
            log.error("Failed to connect to MongoDB", e);
        }
    }

    public MongoCollection<Document> createCollectionIfNotExists(String collectionName) {
        boolean exists = database.listCollectionNames()
                .into(new ArrayList<>())
                .contains(collectionName);
        if(!exists){
            database.createCollection(collectionName);
            log.info("Collection '{}' created successfully.", collectionName);
        } else {
            log.info("Collection '{}' already exists.", collectionName);
        }
        return database.getCollection(collectionName);
    }

    public void close(){
        if (mongoClient != null) {
            mongoClient.close();
            log.info("MongoDB connection closed.");
        }
    }

}

