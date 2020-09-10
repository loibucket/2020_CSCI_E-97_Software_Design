package com.cscie97.ledger.test;

import com.cscie97.ledger.*;

public class TestDriver {
    public static void main(String[] args) {
        CommandProcessor.processCommandFile(args[0]);
    }
}
