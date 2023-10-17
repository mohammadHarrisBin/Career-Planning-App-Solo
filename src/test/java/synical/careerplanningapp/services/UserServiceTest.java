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
    void registerLongUserNameTest() {
        assertFalse(register("ThisUsernameIsWayTooLong", GLOBAL_PASSWORD, "member"));
    }

    @Test
    void registerInvalidPasswordTest() {
        assertFalse(register("username", "pass", "member"));
        assertFalse(register("username", "password", "member"));
        assertFalse(register("username", "PASSWORD", "member"));
        assertFalse(register("username", "Password", "member"));
        assertFalse(register("username", "Pass4word", "member"));
    }

    @Test
    void registerValidRequirementsTest() {
        assertTrue(register("tester", GLOBAL_PASSWORD, "member"));
        assertTrue(register("Tester", "Th!sIsAValidPass4Word", "admin"));
        assertTrue(register("OnlyTwentyCharacters", "Blu3Colors1?", "member"));
    }

    @Test
    void registerDuplicateEntriesTest() {
        assertFalse(register("tester", GLOBAL_PASSWORD, "member"));
        assertFalse(register("tester", GLOBAL_PASSWORD, "admin"));
        assertFalse(register("Tester", GLOBAL_PASSWORD, "member"));
        assertFalse(register("Tester", GLOBAL_PASSWORD, "admin"));
    }

    @Test
    void loginExistingAccountTest() {
        assertTrue(login("admin", GLOBAL_PASSWORD));
    }

    @Test
    void loginNonExistingAccountTest() {
        assertFalse(login("non-existent", "non-existent"));
    }

    @Test
    void loginWrongPasswordTest() {
        assertFalse(login("admin", "wrong-password"));
    }

    @Test
    void viewAccountDetailsExistingUserTest() {
        assertNotNull(viewAccountDetails("admin"));
    }

    @Test
    void viewAccountDetailsNonExistingUserTest() {
        assertNull(viewAccountDetails("non-existent"));
    }

    @Test
    void viewAllAccountDetailsTest() {
        assertNotNull(viewAllAccountDetails());
    }

    @Test
    void deleteExistingUserTest() {
        assertTrue(deleteUser("tester"), "Failed to delete user account 1!");
        assertTrue(deleteUser("Tester"), "Failed to delete user account 2!");
        assertTrue(deleteUser("OnlyTwentyCharacters"), "Failed to delete user account 3!");
    }

    @Test
    void deleteNonExistingUserTest() {
        assertFalse(deleteUser("non-existent"));
    }
}