package synical.careerplanningapp.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import synical.careerplanningapp.lib.DBUtil;
import synical.careerplanningapp.lib.Function;

import static synical.careerplanningapp.lib.Function.*;

public class UserService {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final MongoCollection<Document> collection = DBUtil.getCollection("user");

    // register new user account
    public static boolean register(String iUsername, String iPassword, String accountType) {
        // check for any existing username
        Document query = new Document("username", iUsername);
        Document document = DBUtil.getDocument(collection, query);

        // no same username exist
        if (document == null) {
            Document userDocument = new Document(
                    "username", iUsername)
                    .append("password", Function.encode(iPassword))
                    .append("accountType", accountType)
                    .append("locked", false)
                    .append("attempts", 0);

            InsertOneResult result = DBUtil.insertOne(collection, userDocument);

            // check if insert is successful
            if (result.wasAcknowledged()) {
                print("Successfully registered new user account!");
                return true;
            }
            else {
                warn("Failed to register new user account.");
            }
        }
        else {
            warn("Username '" + iUsername + "' already exist. Try another username.");
        }
        return false;
    }

    // delete user account
    public static boolean deleteUser(String iUsername) {
        // check if username exist
        Document query = new Document("username", iUsername);
        Document document = DBUtil.getDocument(collection, query);

        // user account exist
        if (document != null) {
            DeleteResult result = DBUtil.deleteOne(collection, document);

            // check if delete is successful
            if (result.wasAcknowledged()) {
                print("Successfully deleted user account: " + iUsername + "!");
                return true;
            }
            else {
                warn("Failed to delete user account: " + iUsername + ".");
            }
        }
        else {
            warn("Could not find account with the username: " + iUsername + ".");
        }
        return false;
    }

    // log in to account
    public static boolean login(String iUsername, String iPassword) {
        // check if username exist
        Document query = new Document("username", iUsername);
        Document document = DBUtil.getDocument(collection, query);

        // user account exist
        if (document != null) {
            String password = document.getString("password");
            boolean locked = document.getBoolean("locked");
            int attempts = document.getInteger("attempts");

            // account locked
            if (locked) {
                error("Account is locked. Contact the administrator for help");
                return false;
            }

            if (Function.encode(iPassword).equals(password)) {
                Document resetAttempts = new Document("$set", new Document("attempts", 0));
                UpdateResult result = DBUtil.updateOne(collection, query, resetAttempts);

                if (result.wasAcknowledged()) {
                    print("Successfully log in to account!");
                    return true;
                }
            }
            else {
                Document updateAttempts = new Document("$set", new Document("attempts", attempts += 1));
                UpdateResult result = DBUtil.updateOne(collection, query, updateAttempts);

                if (result.wasAcknowledged()) {
                    if (attempts >= MAX_LOGIN_ATTEMPTS) {
                        Document setLocked = new Document("$set", new Document("locked", true));
                        UpdateResult lockedResult = DBUtil.updateOne(collection, query, setLocked);

                        if (lockedResult.wasAcknowledged()) {
                            error("You have entered the wrong password too many times. This account has been locked.");
                            error("Please contact the administrator to reset your password.");
                        }
                    }
                    else {
                        warn("Incorrect password. Try again.");
                        warn("You have " + (MAX_LOGIN_ATTEMPTS - attempts) + " attempts left.");
                    }
                    return false;
                }
            }
        }
        else {
            warn("Could not find account with the username: " + iUsername + ".");
        }
        return false;
    }

    // view all account details
    public static Document viewAllAccountDetails() {
        return (Document) DBUtil.getAllDocument(collection);
    }

    // view account details
    public static String viewAccountDetails(String username) {
        Document query = new Document("username", username);
        return DBUtil.getDocument(collection, query).toJson();
    }
}