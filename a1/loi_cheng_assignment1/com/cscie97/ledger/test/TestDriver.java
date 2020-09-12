package com.cscie97.ledger.test;

import com.cscie97.ledger.*;

/**
 * Processses a set of command in a text file specified when TestDriver is
 * called
 */
public class TestDriver {
    public static void main(String[] args) {
        CommandProcessor.processCommandFile(args[0]);
    }
}
