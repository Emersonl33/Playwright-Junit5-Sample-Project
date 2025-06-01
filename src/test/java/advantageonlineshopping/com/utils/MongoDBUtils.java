package advantageonlineshopping.com.utils;

import advantageonlineshopping.com.data.GlobalRegisterData;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MongoDBUtils {

    private static final Logger log = LoggerFactory.getLogger(MongoDBUtils.class);
    private static final String CONNECTION_URI = "mongodb://localhost:27017";

    public static MongoClient mongoClient;
    public static MongoDatabase database;
    public static MongoCollection<Document> lastCollection;
    public static Document lastInsertedDocument;

    /**
     * Establishes a connection to the MongoDB server using the specified URI
     * and selects the target database by name.
     *
     * @param databaseName Name of the MongoDB database to connect to.
     */
    public static void init(String databaseName) {
        try {
            ConnectionString connectionString = new ConnectionString(CONNECTION_URI);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(databaseName);

            log.info("Successfully connected to MongoDB. Using database '{}'.", databaseName);
        } catch (Exception e) {
            log.error("An error occurred while trying to connect to MongoDB.", e);
            throw e;
        }
    }

    /**
     * Checks if a collection with the given name exists in the current database.
     * If it doesn't exist, the method creates the collection.
     * Finally, it returns a reference to the collection.
     *
     * @param collectionName The name of the collection to create or retrieve.
     * @return A reference to the MongoCollection object.
     */
    public static MongoCollection<Document> createCollectionIfNotExists(String collectionName) {
        if (database == null) throw new IllegalStateException("MongoDB connection has not been initialized.");

        boolean exists = database.listCollectionNames()
                .into(new ArrayList<>())
                .contains(collectionName);

        if (!exists) {
            database.createCollection(collectionName);
            log.info("Collection '{}' did not exist and was successfully created.", collectionName);
        } else {
            log.info("Collection '{}' already exists in the database.", collectionName);
        }

        lastCollection = database.getCollection(collectionName);
        return lastCollection;
    }

    /**
     * Inserts a new document into the specified collection using the given data map.
     * If the collection doesn't exist, it will be created first.
     *
     * @param collectionName The name of the target collection.
     * @param data           The key-value data to insert as a document.
     */
    public static void insertDocument(String collectionName, Map<String, Object> data) {
        MongoCollection<Document> collection = createCollectionIfNotExists(collectionName);
        Document doc = new Document(data);
        collection.insertOne(doc);

        lastInsertedDocument = doc;

        log.info("A new document was inserted into collection '{}'.", collectionName);
    }

    /**
     * Constructs and saves a registration data document based on values stored in
     * the GlobalRegisterData class. The document is inserted into the specified collection.
     * An _id is generated using the username to uniquely identify the document.
     *
     * @param collectionName The name of the collection where the data will be stored.
     */
    public static void saveRegisterData(String collectionName) {
        if (database == null) throw new IllegalStateException("MongoDB connection has not been initialized.");

        MongoCollection<Document> collection = createCollectionIfNotExists(collectionName);

        Map<String, Object> data = new HashMap<>();
        data.put("_id", GlobalRegisterData.USERNAME + " register data"); // Unique document identifier
        data.put("username", GlobalRegisterData.USERNAME);
        data.put("email", GlobalRegisterData.EMAIL);
        data.put("password", GlobalRegisterData.PASSWORD);
        data.put("confirmPassword", GlobalRegisterData.CONFIRM_PASSWORD);
        data.put("firstname", GlobalRegisterData.FIRSTNAME);
        data.put("lastname", GlobalRegisterData.LASTNAME);
        data.put("phoneNumber", GlobalRegisterData.PHONE_NUMBER);
        data.put("country", GlobalRegisterData.COUNTRY);
        data.put("city", GlobalRegisterData.CITY);
        data.put("address", GlobalRegisterData.ADDRESS);
        data.put("state", GlobalRegisterData.STATE);
        data.put("postalCode", GlobalRegisterData.POSTAL_CODE);

        Document doc = new Document(data);
        collection.insertOne(doc);
        lastInsertedDocument = doc;

        log.info("Registration data successfully inserted with _id: {}", data.get("_id"));
    }

    /**
     * Closes the current MongoDB connection and clears all stored references.
     * This method should be called when the application no longer needs access
     * to the database to properly release resources.
     */
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            lastCollection = null;
            lastInsertedDocument = null;

            log.info("MongoDB connection has been closed and all references cleared.");
        }
    }

}


