package com.yudildev.logic;

import com.yudildev.exceptions.InvalidPasswordException;
import com.yudildev.exceptions.InvalidUsernameException;
import com.yudildev.model.User;
import com.yudildev.tests.TestResults;
import com.yudildev.tests.TestSuite;

public class TestAddActivity implements TestSuite {

    private final TestResults results = new TestResults();

    @Override
    public String name() {
        return "Add Activity Test (create user)";
    }

    @Override
    public TestResults runTests() {
        testCreateUser();

        return results;
    }

    private void testCreateUser() {

        User u;

        try {
            u =(new User.Builder()).username("").password("asd").build();
            results.fail("User shouldn't be created");
        } catch (InvalidUsernameException e) {
            results.pass("Invalid Username!");
        } catch (InvalidPasswordException e) {
            results.fail("Shouldn't have reached here");
        }
    }
}
