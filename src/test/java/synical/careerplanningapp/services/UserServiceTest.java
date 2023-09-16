package synical.careerplanningapp.services;

import org.junit.jupiter.api.*;
import synical.careerplanningapp.lib.DBUtil;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static synical.careerplanningapp.services.UserService.*;

class UserServiceTest {
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
        // register member with normal case
        assertTrue(register("tester", "password", "member"), "Failed to register user account 1");

        // duplicate entry member / admin with normal case
        assertFalse(register("tester", "password", "member"), "Able to register account with duplicate username 1");
        assertFalse(register("tester", "password", "admin"), "Able to register account with duplicate username with different role 1");

        // register admin with all caps
        assertTrue(register("Tester", "password", "admin"), "Failed to register user account 2");

        // duplicate entry member / admin with all caps
        assertFalse(register("Tester", "password", "member"), "Able to register account with duplicate username 2");
        assertFalse(register("Tester", "password", "admin"), "Able to register account with duplicate username with different role 2");
    }

    @Test
    void loginTest() {
        // exist
        assertTrue(login("admin", "admin"), "Failed to login to account!");

        // non existent
        assertFalse(login("nonexistent", "nonexistent"), "Able to login to non-existent account!");

        // correct username, incorrect password
        assertFalse(login("admin", "wrong-password"), "Able to login to account with wrong password!");
    }

    @Test
    void deleteUserTest() {
        // delete account
        assertTrue(deleteUser("tester"), "Failed to delete user account 1!");
        assertTrue(deleteUser("Tester"), "Failed to delete user account 2!");

        // non-existent account
        assertFalse(deleteUser("non-existent"), "Able to delete a non-existent account!");
    }
}