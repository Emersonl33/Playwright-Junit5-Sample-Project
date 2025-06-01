package advantageonlineshopping.com.utils;

import advantageonlineshopping.com.GlobalRegisterData;
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
     * Inicializa a conexão com o MongoDB e seleciona o banco de dados.
     */
    public static void init(String databaseName) {
        try {
            ConnectionString connectionString = new ConnectionString(CONNECTION_URI);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(databaseName);

            log.info("Connected to MongoDB database '{}'", databaseName);
        } catch (Exception e) {
            log.error("Failed to connect to MongoDB", e);
            throw e;
        }
    }

    /**
     * Cria a coleção se não existir e retorna ela.
     */
    public static MongoCollection<Document> createCollectionIfNotExists(String collectionName) {
        if (database == null) throw new IllegalStateException("MongoDB is not initialized");

        boolean exists = database.listCollectionNames()
                .into(new ArrayList<>())
                .contains(collectionName);

        if (!exists) {
            database.createCollection(collectionName);
            log.info("Collection '{}' created successfully.", collectionName);
        } else {
            log.info("Collection '{}' already exists.", collectionName);
        }

        lastCollection = database.getCollection(collectionName);
        return lastCollection;
    }

    /**
     * Insere um documento com os dados fornecidos.
     */
    public static void insertDocument(String collectionName, Map<String, Object> data) {
        MongoCollection<Document> collection = createCollectionIfNotExists(collectionName);
        Document doc = new Document(data);
        collection.insertOne(doc);

        lastInsertedDocument = doc;

        log.info("Documento inserido com sucesso na coleção '{}'", collectionName);
    }

    public static void saveRegisterData(String collectionName) {
        if (database == null) throw new IllegalStateException("MongoDB is not initialized");

        MongoCollection<Document> collection = createCollectionIfNotExists(collectionName);

        Map<String, Object> data = new HashMap<>();
        data.put("_id", GlobalRegisterData.USERNAME + " register data"); // Nome personalizado do documento
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

        log.info("Dados inseridos no MongoDB com _id: {}", data.get("_id"));
    }

    /**
     * Fecha a conexão com o MongoDB.
     */
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            lastCollection = null;
            lastInsertedDocument = null;
            log.info("MongoDB connection closed.");
        }
    }
}


