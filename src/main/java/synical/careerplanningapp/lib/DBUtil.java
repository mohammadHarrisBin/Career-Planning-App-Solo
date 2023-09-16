package synical.careerplanningapp.lib;

import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import javafx.css.converter.DurationConverter;
import org.bson.Document;

import static synical.careerplanningapp.lib.Function.print;

public class DBUtil {
    private static final String URL = "mongodb+srv://root:s03fnXYOwqBLMGw7@cluster1.xhobdxi.mongodb.net";
    private static final String DATABASE = "master";

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    // establish database connection
    public static void init() {
        try {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(URL)).build();
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(DATABASE);

            database.runCommand(new Document("ping", 1));
            print("Successfully established connection to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // close database connection
    public static void close() {
        try {
            mongoClient.close();
            print("Successfully closed connection to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // return collection
    public static MongoCollection<Document> getCollection(String cName) {
        return database.getCollection(cName);
    }

    // get document entry
    public static Document getDocument(String cName, Document query) {
        MongoCollection<Document> collection = getCollection(cName);
        return collection.find(query).first();
    }

    // insert entry
    public static InsertOneResult insertOne(MongoCollection<Document> collection, Document document) {
        return collection.insertOne(document);
    }

    // delete entry
    public static DeleteResult deleteOne(MongoCollection<Document> collection, Document document) {
        return collection.deleteOne(document);
    }
}