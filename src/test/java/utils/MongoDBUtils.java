package utils;

import advantageonlineshopping.com.data.GlobalRegisterDataShopping;
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
        data.put("username", GlobalRegisterDataShopping.USERNAME);
        data.put("email", GlobalRegisterDataShopping.EMAIL);
        data.put("password", GlobalRegisterDataShopping.PASSWORD);
        data.put("confirmPassword", GlobalRegisterDataShopping.CONFIRM_PASSWORD);
        data.put("firstname", GlobalRegisterDataShopping.FIRSTNAME);
        data.put("lastname", GlobalRegisterDataShopping.LASTNAME);
        data.put("phoneNumber", GlobalRegisterDataShopping.PHONE_NUMBER);
        data.put("country", GlobalRegisterDataShopping.COUNTRY);
        data.put("city", GlobalRegisterDataShopping.CITY);
        data.put("address", GlobalRegisterDataShopping.ADDRESS);
        data.put("state", GlobalRegisterDataShopping.STATE);
        data.put("postalCode", GlobalRegisterDataShopping.POSTAL_CODE);

        Document doc = new Document(data);
        collection.insertOne(doc);
        lastInsertedDocument = doc;

        log.info("Registration data successfully inserted with _id: {}", data.get("_id"));
    }

    /**
     * Loads the first document from the specified collection and populates GlobalRegisterData fields.
     * Also updates the lastCollection and lastInsertedDocument fields.
     *
     * @param collectionName The name of the collection to search in.
     * @return true if a document was found and GlobalRegisterData was populated, false otherwise.
     */
    public static boolean loadLastRegisterData(String collectionName) {
        if (database == null) throw new IllegalStateException("MongoDB connection has not been initialized.");

        lastCollection = database.getCollection(collectionName);
        Document foundDocument = lastCollection.find()
                .sort(new Document("_id", -1))
                .first();

        if (foundDocument != null) {
            lastInsertedDocument = foundDocument;

            GlobalRegisterDataShopping.USERNAME = foundDocument.getString("username");
            GlobalRegisterDataShopping.EMAIL = foundDocument.getString("email");
            GlobalRegisterDataShopping.PASSWORD = foundDocument.getString("password");
            GlobalRegisterDataShopping.CONFIRM_PASSWORD = foundDocument.getString("confirmPassword");
            GlobalRegisterDataShopping.FIRSTNAME = foundDocument.getString("firstname");
            GlobalRegisterDataShopping.LASTNAME = foundDocument.getString("lastname");
            GlobalRegisterDataShopping.PHONE_NUMBER = foundDocument.getString("phoneNumber");
            GlobalRegisterDataShopping.COUNTRY = foundDocument.getString("country");
            GlobalRegisterDataShopping.CITY = foundDocument.getString("city");
            GlobalRegisterDataShopping.ADDRESS = foundDocument.getString("address");
            GlobalRegisterDataShopping.STATE = foundDocument.getString("state");
            GlobalRegisterDataShopping.POSTAL_CODE = foundDocument.getString("postalCode");

            log.info("First document loaded and GlobalRegisterData populated.");
            return true;
        } else {
            log.error("No documents found in collection '{}'.", collectionName);
            return false;
        }
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


