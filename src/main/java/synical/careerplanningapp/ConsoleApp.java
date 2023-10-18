package synical.careerplanningapp;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import synical.careerplanningapp.lib.DBUtil;
import synical.careerplanningapp.lib.Function;
import synical.careerplanningapp.services.JobService;
import synical.careerplanningapp.services.UserService;

import java.time.LocalDateTime;

import static synical.careerplanningapp.lib.Function.*;

public class ConsoleApp {
    private static final boolean DEBUG = true;

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

                    boolean success = false;
                    String iUsername = "admin";
                    String iPassword = "Cy@nColors1?";
                    if (!DEBUG) {
                        iUsername = Function.getUserInputString("Username > ");
                        iPassword = Function.getUserInputString("Password > ");
                    }
                    success = UserService.login(iUsername, iPassword);

                    if (success) {
                        username = iUsername;
                        start();
                    }
                } else if (option == 2) {
                    // sign up new account
                    header("registering new account.");

                    String iUsername = Function.getUserInputString("Username > ");
                    String iPassword = Function.getUserInputString("Password > ");

                    // keep asking until valid
                    boolean valid = UserService.sanityChecker(iUsername, iPassword);
                    while (!valid) {
                        iUsername = Function.getUserInputString("Username > ");
                        iPassword = Function.getUserInputString("Password > ");

                        valid = UserService.sanityChecker(iUsername, iPassword);
                    }

                    UserService.register(iUsername, iPassword, "member");
                } else if (option == 3) {
                    // stops application
                    print("Career planning application has stopped.");

                    // close database connection
                    DBUtil.close();
                    return;
                } else {
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
                        } else if (subOption == 2) {
                            UserService.viewAllAccountDetails();
                        } else if (subOption == 3) {
                            boolean success = UserService.deleteUser(username);

                            // user deleted their account
                            // bring user back to log in screen
                            if (success) {
                                login();
                                return;
                            }
                        }
                    } else {
                        displayMemberUserMenu();

                        subOption = Function.getUserInputInt("Enter option > ");
                        if (subOption == 1) {
                            UserService.viewAccountDetails(username);
                        }
                    }
                }
            } else if (mainOption == 2) {
                // job opportunity menu
                // TODO: display sub choices for job opportunity
                print("display job opportunity menu");

                int subOption = -1;

                while (subOption != 0) {
                    if (isAdmin) {
                        displayAdminJobMenu();

                        subOption = Function.getUserInputInt("Enter option > ");
                        if (subOption == 1) {
                            JobService.viewJobOpportunities();
                        } else if (subOption == 2) {
                            String title = Function.getUserInputString("Enter job title > ");
                            String description = Function.getUserInputString("Enter job description > ");
                            String companyName = Function.getUserInputString("Enter company name > ");
                            String addressLine1 = Function.getUserInputString("Enter address line 1 > ");
                            String addressLine2 = Function.getUserInputString("Enter address line 2 > ");
                            double salaryMin = Function.getUserInputDouble("Enter min. salary > $");
                            double salaryMax = Function.getUserInputDouble("Enter max. salary > $");
                            String[] qualifications = Function.getUserInputStringList("Enter qualifications");
                            String[] skills = Function.getUserInputStringList("Enter skills");
                            String responsibilities = Function.getUserInputString("Enter responsibilities > ");
                            String education = Function.getUserInputString("Enter education required > ");
                            String applicationDateline = Function.getUserInputString("Enter application dateline (dd/mm/yyyy) > "); // November 1, 2023
                            int applicationProcessTime = Function.getUserInputInt("Enter application processing time (days) > "); // 7 days for the application process
                            boolean requiresTravel = Function.getUserInputBoolean("Requires travel? (true/false) > ");
                            boolean allowsRemoteWork = Function.getUserInputBoolean("Allow remote work? (true/false) > ");
                            String languageRequirements = Function.getUserInputString("Language requirements > ");
                            String physicalRequirements = Function.getUserInputString("Physical requirements > ");

                            JobService.register(title, description, companyName, addressLine1, addressLine2, salaryMin, salaryMax, qualifications, skills, responsibilities, education, applicationDateline, applicationProcessTime, requiresTravel, allowsRemoteWork, languageRequirements, physicalRequirements, username);
                        }
                    } else {
                        displayMemberJobMenu();

                        subOption = Function.getUserInputInt("Enter option > ");
                        if (subOption == 1) {
                            JobService.viewJobOpportunities();
                        }
                    }
                }
            } else if (mainOption == 3) {
                // resume menu
                // TODO: display sub choices for resume

                print("display resume menu");
            } else if (mainOption == 4) {
                // skills menu
                // TODO: display sub choices for skills

                print("display skills menu");
            } else if (mainOption == 5) {
                // career menu
                // TODO: display sub choices for career

                print("display career menu");
            } else if (mainOption == 6) {
                // assessment menu
                // TODO: display sub choices for assessment

                print("display assessment menu");
            } else if (mainOption == 7) {
                // log out of account

                print("Logged out.");
                username = "";

                // bring user back to log in menu
                login();
                return;
            } else {
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

    // member job opportunity menu
    private static void displayMemberJobMenu() {
        String title = "JOB OPPORTUNITY MENU";
        String[] options = {"View job opportunity", "<<< Back"};
        Function.displayMenu(title, options);
    }

    // admin job opportunity menu
    private static void displayAdminJobMenu() {
        String title = "JOB OPPORTUNITY MENU";
        String[] options = {"View job opportunity", "Add job opportunity", "Delete job opportunity", "<<< Back"};
        Function.displayMenu(title, options);
    }
}