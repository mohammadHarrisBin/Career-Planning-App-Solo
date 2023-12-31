package synical.careerplanningapp.services;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import synical.careerplanningapp.lib.DBUtil;
import synical.careerplanningapp.lib.Function;

import static synical.careerplanningapp.lib.Function.*;

public class UserService {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final MongoCollection<Document> collection = DBUtil.getCollection("user");

    private static final String REGEX_DIGIT = "^.*[0-9].*";
    private static final String REGEX_LOWERCASE = ".*[a-z].*";
    private static final String REGEX_UPPERCASE = ".*[A-Z].*";
    private static final String REGEX_SYMBOLS = ".*[!@#$&*?].*";

    // register new user account
    public static boolean register(String iUsername, String iPassword, String accountType) {
        if (!sanityChecker(iUsername, iPassword)) return false;

        // get timestamp
        long timestamp = Function.getCurrentTimestamp();

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
                    .append("attempts", 0)
                    .append("createdAt", timestamp)
                    .append("lastOnline", timestamp);

            boolean result = DBUtil.insertOne(collection, userDocument);

            // check if insert is successful
            if (result) {
                print("Successfully registered new user account!");
                return true;
            } else {
                error("Failed to register new user account.");
            }
        } else {
            error("Username '" + iUsername + "' already exist. Try another username.");
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
            boolean result = DBUtil.deleteOne(collection, document);

            // check if delete is successful
            if (result) {
                print("Successfully deleted user account: " + iUsername + "!");
                return true;
            } else {
                error("Failed to delete user account: " + iUsername + ".");
            }
        } else {
            error("Could not find account with the username: " + iUsername + ".");
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
            long timestamp = Function.getCurrentTimestamp();

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
                boolean resetResult = DBUtil.updateOne(collection, query, resetAttempts);

                Document lastOnline = new Document("$set", new Document("lastOnline", timestamp));
                boolean updateLastOnlineResult = DBUtil.updateOne(collection, query, lastOnline);

                if (resetResult && updateLastOnlineResult) {
                    print("Successfully log in to account!");
                    return true;
                } else {
                    error("Failed to update database while user log in!");
                }
            } else {
                Document updateAttempts = new Document("$set", new Document("attempts", attempts += 1));
                boolean result = DBUtil.updateOne(collection, query, updateAttempts);

                if (result) {
                    if (attempts >= MAX_LOGIN_ATTEMPTS) {
                        Document setLocked = new Document("$set", new Document("locked", true));
                        boolean lockedResult = DBUtil.updateOne(collection, query, setLocked);

                        if (lockedResult) {
                            error("You have entered the wrong password too many times. This account has been locked.");
                            error("Please contact the administrator to reset your password.");
                        }
                    } else {
                        warn("Incorrect password. Try again.");
                        warn("You have " + (MAX_LOGIN_ATTEMPTS - attempts) + " attempts left.");
                    }
                    return false;
                }
            }
        } else {
            error("Could not find account with the username: " + iUsername + ".");
        }
        return false;
    }

    // view all account details
    public static String viewAllAccountDetails() {
        String output = null;
        FindIterable<Document> documents = DBUtil.getAllDocument(collection);

        for (Document document : documents) {
            output += document.toJson() + "\n";
        }

        if (output != null) {
            output = output.trim();
        } else {
            error("There is no data in the collection '" + collection.getNamespace() + "'!");
        }

        print(output);
        return output;
    }

    // view account details
    public static String viewAccountDetails(String iUsername) {
        Document query = new Document("username", iUsername);
        Document document = DBUtil.getDocument(collection, query);
        if (document != null) {
            print(document.toJson());
            return document.toJson();
        } else {
            error("Could not find username '" + iUsername + "' account details!");
        }
        return null;
    }

    // password validator
    public static boolean sanityChecker(String iUsername, String iPassword) {
        if (iUsername.length() > 20) {
            warn("Username can only be 20 characters long.");
            return false;
        }

        if (iPassword.length() < 8) {
            warn("Password should be at least 8 characters long.");
            return false;
        }

        if (!iPassword.matches(REGEX_UPPERCASE)) {
            warn("Password should contain at least one uppercase character.");
            return false;
        }

        if (!iPassword.matches(REGEX_LOWERCASE)) {
            warn("Password should contain at least one lowercase character.");
            return false;
        }

        if (!iPassword.matches(REGEX_DIGIT)) {
            warn("Password should contain at least one digit.");
            return false;
        }

        if (!iPassword.matches(REGEX_SYMBOLS)) {
            warn("Password should contain at least one of these symbols: '!, @, #, $, &, *, ?'.");
            return false;
        }

        return true;
    }
}