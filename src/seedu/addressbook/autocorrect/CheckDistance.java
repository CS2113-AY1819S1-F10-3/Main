package seedu.addressbook.autocorrect;

import java.util.ArrayList;

/**
 * Checks the number of single character changes required to convert one string to another.
 */
public class CheckDistance {

    public static String checkDistance(String commandInput) {
        Dictionary dictionary = new Dictionary();
        EditDistance calculate = new EditDistance();
        String prediction = "none";
        ArrayList<String> commandList = dictionary.getCommands();
        if(commandList.isEmpty())
            System.out.println("Empty");
        int distance, check = 0;
        for (String command : commandList) {
            distance = calculate.computeDistance(commandInput, command);
            if (distance == 1) {
                prediction = command;
                check = 1;
                break;
            }
        }
        if (check == 0) {
            prediction = "none";
        }
        return prediction;
    }
}
