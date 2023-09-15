package synical.careerplanningapp;

import synical.careerplanningapp.services.UserService;
import synical.careerplanningapp.lib.DBUtil;
import synical.careerplanningapp.lib.Function;

import static synical.careerplanningapp.lib.Function.print;

public class ConsoleApp {
    private static int userid = 0;

    public static void main(String[] args) {
        print("Career planning app has started.");

        //TODO: set up database connection
        DBUtil.init();

        login();
    }

    private static void login() {
        while (userid == 0) {
            int option = -1;

            while (option != 0) {
                displayHomePageMenu();
                option = Function.getUserInputInt("Enter option > ");

                if (option == 1) {
                    //TODO: create user login

                    print("User chose log in.");
                    userid = UserService.login();
                }
                else if (option == 2) {
                    //TODO: create user signup
                    print("User chose sign up.");
                }
                else if (option == 3) {
                    print("Career planning application has stopped.");
                    DBUtil.close();
                    return;
                }
                else {
                    print("Invalid option. Re-enter choice.");
                }
            }
        }
    }

    private static void start() {
        int mainOption = -1;

        while (mainOption != 0) {
            displayMainPageMenu();
            mainOption = Function.getUserInputInt("Enter option > ");

            if (mainOption == 1) {
                //TODO: USER
                continue;
            }
            else if (mainOption == 2) {
                //TODO: JOB OPPORTUNITY
                continue;
            }
            else if (mainOption == 3) {
                //TODO: RESUME
                continue;
            }
            else if (mainOption == 4) {
                //TODO: SKILL
                continue;
            }
            else if (mainOption == 5) {
                //TODO: CAREER
                continue;
            }
            else if (mainOption == 6) {
                //TODO: ASSESSMENT
                continue;
            }
            else if (mainOption == 7) {
                print("Logged out.");
                userid = 0;
                login();
            }
            else {
                print("Invalid option. Re-enter option.");
            }
        }
    }

    private static void displayHomePageMenu() {
        String title = "WELCOME TO CAREER PLANNING, LOG-IN OR SIGN-UP TO GET STARTED.";
        String[] options = {"Log-in", "Sign-up", "Exit"};
        Function.displayMenu(title, options);
    }

    private static void displayMainPageMenu() {
        String title = "SELECT CATEGORY TO EXPLORE.";
        String[] options = {"User menu", "Job opportunity menu", "Resume menu", "Skill menu", "Career menu", "Assessment menu", "Log out"};
        Function.displayMenu(title, options);
    }
}