package ca.umanitoba.cs.pariyary.ui;

import java.util.Scanner;

public class LoggedOutMenuDisplay {
    private final Scanner input;

    public LoggedOutMenuDisplay(Scanner input) {
        this.input = input;
    }

    public String getChoice() {
        System.out.println("\n1. Login");
        System.out.println("2. Create User");
        System.out.println("3. Exit");
        System.out.print("Enter choice (1 - 3): ");
        return input.nextLine();
    }
}