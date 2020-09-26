package cscie97.smartcity.test;

import cscie97.smartcity.model.*;
import java.lang.String;

/**
 * Process a set of command in a text file specified when TestDriver is
 * called
 */
public class TestDriver {
    public static void main(String[] args) {

        CommandAPI commandAPI = new CommandAPI();
        commandAPI.processCommandFile("placeholder",args[0]);

    }
}

//compile command javac cscie97/smartcity/model/*.java cscie97/smartcity/test/*.java
//test command java -cp . cscie97.smartcity.test.TestDriver smartcity_model.script
