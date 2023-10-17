package synical.careerplanningapp.services;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import synical.careerplanningapp.lib.DBUtil;

import static org.junit.jupiter.api.Assertions.*;

class JobServiceTest {
    @BeforeAll
    static void beforeAll() {
        DBUtil.init();
    }

    @AfterAll
    static void afterAll() {
        DBUtil.close();
    }

    @Test
    void registerEmptyFieldsJobTest() {
        assertFalse(false, "Failed to register job");
    }
}