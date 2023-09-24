package synical.careerplanningapp.services;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import synical.careerplanningapp.lib.DBUtil;

import static org.junit.jupiter.api.Assertions.*;
import static synical.careerplanningapp.services.UserService.*;

class UserServiceTest {
    private static final String GLOBAL_PASSWORD = "Cy@nColors1?";

    @BeforeAll
    static void beforeAll() {
        DBUtil.init();
    }

    @AfterAll
    static void afterAll() {
        DBUtil.close();
    }

    @Test
    void registerTest() {
        // test register account long username
        assertFalse(register("ThisUsernameIsWayTooLong", GLOBAL_PASSWORD, "member"), "Able to register account that exceeds username length!");

        // test register account with invalid password requirements
        assertFalse(register("username", "pass", "member"), "Able to register account that has password too short!");
        assertFalse(register("username", "password", "member"), "Able to register account that has no uppercase, digits, symbols!");
        assertFalse(register("username", "PASSWORD", "member"), "Able to register account that has no lowercase, digits, symbols!");
        assertFalse(register("username", "Password", "member"), "Able to register account that has no digits or symbols!");
        assertFalse(register("username", "Pass4word", "member"), "Able to register account that has no symbols!");

        // test register account with valid requirements
        assertTrue(register("tester", GLOBAL_PASSWORD, "member"), "Failed to register user account 1!");
        assertTrue(register("Tester", "Th!sIsAValidPass4Word", "admin"), "Failed to register user account 2!");
        assertTrue(register("OnlyTwentyCharacters", "Blu3Colors1?", "member"), "Failed to register user account 3!");

        // test register account with duplicate entry member / admin
        assertFalse(register("tester", GLOBAL_PASSWORD, "member"), "Able to register account with duplicate username 1!");
        assertFalse(register("tester", GLOBAL_PASSWORD, "admin"), "Able to register account with duplicate username with different role 1!");

        // test register account with duplicate entry member / admin with all caps
        assertFalse(register("Tester", GLOBAL_PASSWORD, "member"), "Able to register account with duplicate username 2!");
        assertFalse(register("Tester", GLOBAL_PASSWORD, "admin"), "Able to register account with duplicate username with different role 2!");
    }

    @Test
    void loginTest() {
        // test log in for existing account
        assertTrue(login("admin", GLOBAL_PASSWORD), "Failed to login to account!");

        // test log in for non-existent account
        assertFalse(login("non-existent", "non-existent"), "Able to login to non-existent account!");

        // test log in with wrong password
        assertFalse(login("admin", "wrong-password"), "Able to login to account with wrong password!");
    }

    @Test
    void deleteUserTest() {
        // test delete existing account
        assertTrue(deleteUser("tester"), "Failed to delete user account 1!");
        assertTrue(deleteUser("Tester"), "Failed to delete user account 2!");
        assertTrue(deleteUser("OnlyTwentyCharacters"), "Failed to delete user account 3!");

        // test delete non-existent account
        assertFalse(deleteUser("non-existent"), "Able to delete a non-existent account!");
    }

    @Test
    void viewAccountDetailsTest() {
        // test view account details for existing user
        assertNotNull(viewAccountDetails("admin"), "Unable to view admin account details!");

        // test view account details for non-existent user
        assertNull(viewAccountDetails("non-existent"), "Got account details for non-existent account!");
    }

    @Test
    void viewAllAccountDetailsTest() {
        // test view all account details
        assertNotNull(viewAllAccountDetails(), "Unable to view admin account details!");
    }
}