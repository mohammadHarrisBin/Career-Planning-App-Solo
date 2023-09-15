package synical.careerplanningapp.lib;

import java.util.regex.Pattern;

public class Function {
    public static String getUserInputString(String prompt) {
        System.out.print(prompt);
        return new java.util.Scanner(System.in).nextLine();
    }

    public static String getUserInputStringRegex(String prompt, String regex) {
        String input = getUserInputString(prompt);
        boolean matched = Pattern.matches(regex, input);

        while (!matched) {
            System.out.println("Input does not meet criteria.");
            input = getUserInputString(prompt);
            matched = Pattern.matches(regex, input);
        }

        return input;
    }

    public static int getUserInputInt(String prompt) {
        int input = 0;
        boolean valid = false;
        while (!valid) {
            try {
                input = Integer.parseInt(getUserInputString(prompt));
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer value.");
            }
        }

        return input;
    }

    public static double getUserInputDouble(String prompt) {
        double input = 0;
        boolean valid = false;

        while (!valid) {
            try {
                input = Double.parseDouble(getUserInputString(prompt));
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid double value.");
            }
        }

        return input;
    }

    public static char getUserInputChar(String prompt) {
        char input = 0;
        boolean valid = false;

        while (!valid) {
            String temp = getUserInputString(prompt);
            if (temp.length() != 1) {
                System.out.println("Please enter a character.");
            } else {
                input = temp.charAt(0);
                valid = true;
            }
        }

        return input;
    }

    public static void line(String character, int length) {
        System.out.println(character.repeat(length));
    }

    public static void displayMenu(String title, String[] menu) {
        line("=", 100);
        System.out.println(title.toUpperCase());
        line("=", 100);

        for (int i = 0; i < menu.length; i++) {
            System.out.printf("%-3s %s\n", (i + 1) + ".", menu[i]);
        }
    }

    public static void print(String text) {
        System.out.printf(">>> %s\n", text);
    }
}