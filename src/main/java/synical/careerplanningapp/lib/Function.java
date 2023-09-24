package synical.careerplanningapp.lib;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.regex.Pattern;

public class Function {
    public static String getUserInputString(String prompt) {
        System.out.print(Settings.input + prompt);
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
        System.out.println(Settings.normal + character.repeat(length));
    }

    public static void displayMenu(String title, String[] menu) {
        line("=", 100);
        System.out.println(title.toUpperCase());
        line("=", 100);

        for (int i = 0; i < menu.length; i++) {
            System.out.printf(Settings.normal + "%-3s %s\n", (i + 1) + ".", menu[i]);
        }
    }

    public static void header(String title) {
        line("=", 100);
        System.out.println(title.toUpperCase());
        line("=", 100);
    }

    public static void print(String text) {
        System.out.printf(Settings.print + ">>> %s\n", text);
    }

    public static void print(String color, String text) {
        System.out.printf(color + ">>> %s\n", text);
    }

    public static void warn(String text) {
        System.out.printf(Settings.warn + ">>> %s\n", text);
    }

    public static void error(String text) {
        System.out.printf(Settings.error +  ">>> %s\n", text);
    }

    public static String encode(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] encodedHash = digest.digest(text.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static long getCurrentTimestamp() {
        return Instant.now().atZone(ZoneId.of("GMT+8")).toInstant().toEpochMilli();
    }
}