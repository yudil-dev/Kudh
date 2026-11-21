package ca.umanitoba.cs.pariyary.logic;

import ca.umanitoba.cs.pariyary.exceptions.InvalidPasswordException;
import ca.umanitoba.cs.pariyary.exceptions.InvalidUsernameException;
import ca.umanitoba.cs.pariyary.model.User;
import ca.umanitoba.cs.pariyary.tests.TestResults;
import ca.umanitoba.cs.pariyary.tests.TestSuite;

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
