package synical.careerplanningapp;

import synical.careerplanningapp.services.UserService;
import synical.careerplanningapp.lib.DBUtil;
import synical.careerplanningapp.lib.Function;

import static synical.careerplanningapp.lib.Function.print;

public class ConsoleApp {
    private static int userid = 0;

    // MAIN PROGRAM LAUNCHER
    public static void main(String[] args) {
        print("Career planning app has started.");

        // CALL METHOD TO ENABLE CONNECTION TO DATABASE
        DBUtil.init();

        // START USER LOGIN METHOD
        login();
    }

    // HANDLES USER INPUT FOR LOGIN MENU
    private static void login() {
        while (userid == 0) {
            int option = -1;

            while (option != 0) {
                displayHomePageMenu();
                option = Function.getUserInputInt("Enter option > ");

                if (option == 1) {
                    // LOG IN OPTION

                    print("User chose log in.");
                    userid = UserService.login();
                }
                else if (option == 2) {
                    // SIGN UP OPTION
                    print("User chose sign up.");
                }
                else if (option == 3) {
                    // USER CHOSE TO EXIT APP
                    print("Career planning application has stopped.");

                    // CLOSE THE DATABASE CONNECTION
                    DBUtil.close();
                    return;
                }
                else {
                    // INVALID OPTION
                    print("Invalid option. Re-enter choice.");
                }
            }
        }
    }

    // HANDLES USER INPUT FOR MAIN MENU
    private static void start() {
        int mainOption = -1;

        while (mainOption != 0) {
            displayMainPageMenu();
            mainOption = Function.getUserInputInt("Enter option > ");

            if (mainOption == 1) {
                // USER MENU
                continue;
            }
            else if (mainOption == 2) {
                // OPPORTUNITY MENU
                continue;
            }
            else if (mainOption == 3) {
                // RESUME MENU
                continue;
            }
            else if (mainOption == 4) {
                // SKILL MENU
                continue;
            }
            else if (mainOption == 5) {
                // CAREER MENU
                continue;
            }
            else if (mainOption == 6) {
                // ASSESSMENT MENU
                continue;
            }
            else if (mainOption == 7) {
                // LOG OUT OF ACCOUNT
                print("Logged out.");
                userid = 0;

                // START LOGIN MENU METHOD
                login();
            }
            else {
                // INVALID OPTION
                print("Invalid option. Re-enter option.");
            }
        }
    }

    // LOGIN MENU
    private static void displayHomePageMenu() {
        String title = "WELCOME TO CAREER PLANNING, LOG-IN OR SIGN-UP TO GET STARTED.";
        String[] options = {"Log-in", "Sign-up", "Exit"};
        Function.displayMenu(title, options);
    }

    // MAIN MENU
    private static void displayMainPageMenu() {
        String title = "SELECT CATEGORY TO EXPLORE.";
        String[] options = {"User menu", "Job opportunity menu", "Resume menu", "Skill menu", "Career menu", "Assessment menu", "Log out"};
        Function.displayMenu(title, options);
    }
}