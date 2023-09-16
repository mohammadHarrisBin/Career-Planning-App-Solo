package synical.careerplanningapp.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import synical.careerplanningapp.lib.DBUtil;
import synical.careerplanningapp.lib.Function;

import static synical.careerplanningapp.lib.Function.print;

public class UserService {
    private static final MongoCollection<Document> collection = DBUtil.getCollection("user");

    // register new user account
    public static boolean register(String iUsername, String iPassword, String role) {
        // check for any existing username
        Document query = new Document("username", iUsername);
        Document document = DBUtil.getDocument("user", query);

        // no same username exist
        if (document == null) {
            Document userDocument = new Document(
                    "username", iUsername)
                    .append("password", Function.encode(iPassword))
                    .append("role", role);

            InsertOneResult result = DBUtil.insertOne(collection, userDocument);

            // check if insert is successful
            if (result.wasAcknowledged()) {
                print("Successfully registered new user account!");
                return true;
            }
            else {
                print("Failed to register new user account.");
            }
        }
        else {
            print("Username '" + iUsername + "' already exist. Try another username.");
        }
        return false;
    }

    // delete user account
    public static boolean deleteUser(String iUsername) {
        // check if username exist
        Document query = new Document("username", iUsername);
        Document document = DBUtil.getDocument("user", query);

        // user account exist
        if (document != null) {
            DeleteResult result = DBUtil.deleteOne(collection, document);

            // check if delete is successful
            if (result.wasAcknowledged()) {
                print("Successfully deleted user account: " + iUsername + "!");
                return true;
            }
            else {
                print("Failed to delete user account: " + iUsername + ".");
            }
        }
        else {
            print("Could not find account with the username: " + iUsername + ".");
        }
        return false;
    }

    // log in to account
    public static boolean login(String iUsername, String iPassword) {
        // check if username exist
        Document query = new Document("username", iUsername);
        Document document = DBUtil.getDocument("user", query);

        // user account exist
        if (document != null) {
            String password = document.getString("password");

            if (Function.encode(iPassword).equals(password)) {
                print("Successfully log in to account!");
                return true;
            }
            else {
                print("Incorrect password. Try again.");
            }
        }
        else {
            print("Could not find account with the username: " + iUsername + ".");
        }
        return false;
    }
}