package seedu.addressbook.autocorrect;

import java.util.ArrayList;

/**
 * Checks the number of single character changes required to convert one string to another.
 */
public class CheckDistance {

    Dictionary dictionary = new Dictionary();
    EditDistance calculate = new EditDistance();

    ArrayList<String> commandList = dictionary.getCommands();

    String prediction = "none";

    public String checkDistance(String commandInput) {
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

    public Integer checkCommandDistance(String commandInput) {
        int distance, check = 0;
        for (String command : commandList) {
            distance = calculate.computeDistance(commandInput, command);
            if (distance == 0) {
                check = 1;
                break;
            }
        }
        return check;
    }
}
