package synical.careerplanningapp;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import synical.careerplanningapp.services.UserService;
import synical.careerplanningapp.lib.DBUtil;
import synical.careerplanningapp.lib.Function;

import static synical.careerplanningapp.lib.Function.*;

public class ConsoleApp {
    private static String username = "";
    private static MongoCollection<Document> userCollection;

    public static void main(String[] args) {
        print("Career planning app has started.");

        // establish database connection
        DBUtil.init();

        // setup collection var
        userCollection = DBUtil.getCollection("user");

        // start user log in menu
        login();
    }

    // log in handler
    private static void login() {
        while (username.isEmpty()) {
            int option = -1;

            while (option != 0) {
                displayHomePageMenu();
                option = Function.getUserInputInt("Enter option > ");

                if (option == 1) {
                    // log in option
                    header("logging in to account.");

                    String iUsername = Function.getUserInputString("Username > ");
                    String iPassword = Function.getUserInputString("Password > ");

                    boolean success = UserService.login(iUsername, iPassword);
                    if (success) {
                        username = iUsername;
                        start();
                    }
                }
                else if (option == 2) {
                    // sign up new account
                    header("registering new account.");

                    String iUsername = Function.getUserInputString("Username > ");
                    String iPassword = Function.getUserInputString("Password > ");

                    UserService.register(iUsername, iPassword, "member");
                }
                else if (option == 3) {
                    // stops application
                    print("Career planning application has stopped.");

                    // close database connection
                    DBUtil.close();
                    return;
                }
                else {
                    // invalid option
                    warn("Invalid option. Re-enter choice.");
                }
            }
        }
    }

    // main menu handler
    private static void start() {
        int mainOption = -1;
        Document query = new Document("username", username);
        boolean isAdmin = DBUtil.getDocument(userCollection, query).getString("accountType").equals("admin");

        while (mainOption != 0) {
            displayMainPageMenu();
            mainOption = Function.getUserInputInt("Enter option > ");

            if (mainOption == 1) {
                // user menu
                // TODO: display sub choices for user
                int subOption = -1;

                while (subOption != 0) {
                    if (isAdmin) {
                        displayAdminUserMenu();

                        subOption = Function.getUserInputInt("Enter option > ");
                        if (subOption == 1) {
                            UserService.viewAccountDetails(username);
                        }
                        else if (subOption == 2) {
                            UserService.viewAllAccountDetails();
                        }
                    }
                    else {
                        displayMemberUserMenu();

                        subOption = Function.getUserInputInt("Enter option > ");
                        if (subOption == 1) {
                           UserService.viewAccountDetails(username);
                        }
                    }
                }
            }
            else if (mainOption == 2) {
                // job opportunity menu
                // TODO: display sub choices for job opportunity
                print("display job opportunity menu");
            }
            else if (mainOption == 3) {
                // resume menu
                // TODO: display sub choices for resume

                print("display resume menu");
            }
            else if (mainOption == 4) {
                // skills menu
                // TODO: display sub choices for skills

                print("display skills menu");
            }
            else if (mainOption == 5) {
                // career menu
                // TODO: display sub choices for career

                print("display career menu");
            }
            else if (mainOption == 6) {
                // assessment menu
                // TODO: display sub choices for assessment

                print("display assessment menu");
            }
            else if (mainOption == 7) {
                // log out of account

                print("Logged out.");
                username = "";

                // bring user back to log in menu
                login();
                return;
            }
            else {
                // invalid option
                print("Invalid option. Re-enter option.");
            }
        }
    }

    // login menu
    private static void displayHomePageMenu() {
        String title = "WELCOME TO CAREER PLANNING, LOG-IN OR SIGN-UP TO GET STARTED.";
        String[] options = {"Log-in", "Sign-up", "Exit"};
        Function.displayMenu(title, options);
    }

    // main menu
    private static void displayMainPageMenu() {
        String title = "SELECT CATEGORY TO EXPLORE.";
        String[] options = {"User menu", "Job opportunity menu", "Resume menu", "Skill menu", "Career menu", "Assessment menu", "Log out"};
        Function.displayMenu(title, options);
    }

    // member user menu
    private static void displayMemberUserMenu() {
        String title = "USER MENU";
        String[] options = {"View account details", "Delete account", "<<< Back"};
        Function.displayMenu(title, options);
    }

    // admin user menu
    private static void displayAdminUserMenu() {
        String title = "USER MENU";
        String[] options = {"View account details", "View all accounts", "Delete account", "<<< Back"};
        Function.displayMenu(title, options);
    }
}