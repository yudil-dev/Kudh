package ca.umanitoba.cs.pariyary.tests;

import ca.umanitoba.cs.pariyary.logic.TestAddActivity;

import static ca.umanitoba.cs.pariyary.ui.OutputTools.*;

public class TestHarness {
    public static void main(String[] args) {
        printlnColor(PURPLE, "Test Harness ---------------");

        TestSuite[] suites = {
                new TestAddActivity()
        };

        boolean failures = false;

        for (var suite : suites) {
            printlnColor(BLUE, suite.name() + " --------");
            TestResults results = suite.runTests();

            System.out.printf("Suite tests: %d\n", results.totalTests());
            System.out.printf("\tSuccesses: %d\n", results.successes());
            System.out.printf("\tFailures: %d\n", results.failures());

            if (results.failures() > 0) {
                printlnColor(RED, "There were test failures.");
            } else {
                printlnColor(GREEN, "All tests passed!");
            }

            printlnColor(BLUE, "-------------");
            failures = failures || results.failures() > 0;
        }

        printlnColor(PURPLE, "Overall Results ------------");
        if (failures) {
            printlnColor(RED, "There were test failures.");
        } else {
            printlnColor(GREEN, "All tests passed!");
        }
    }
}
