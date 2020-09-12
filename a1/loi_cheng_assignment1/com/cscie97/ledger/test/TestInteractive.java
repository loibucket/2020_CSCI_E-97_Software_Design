package com.cscie97.ledger.test;

import com.cscie97.ledger.*;

import java.util.*;

/**
 * Processses commands line by line from user input
 */
public class TestInteractive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("LEDGER");
        System.out.println("CTRL-C TO EXIT");
        try {
            while (true) {
                System.out.print("COMMAND:");
                String str = sc.nextLine();
                CommandProcessor.processCommand(str, 1);
            }
        } catch (Exception e) {
            System.out.print("FINISHED");
            sc.close();
        }
    }
}
