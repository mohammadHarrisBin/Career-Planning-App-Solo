package synical.careerplanningapp.lib;

import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import org.bson.Document;

import static synical.careerplanningapp.lib.Function.print;

public class DBUtil {
    private static final String URL = "mongodb+srv://root:s03fnXYOwqBLMGw7@cluster1.xhobdxi.mongodb.net";
    private static final String DATABASE = "master";

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    // MAIN METHOD TO START DATABASE CONNECTION
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

    // MAIN METHOD TO CLOSE DATABASE CONNECTION
    public static void close() {
        try {
            mongoClient.close();
            print("Successfully closed connection to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}