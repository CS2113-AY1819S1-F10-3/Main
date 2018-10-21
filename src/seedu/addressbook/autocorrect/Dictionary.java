package seedu.addressbook.autocorrect;

import java.util.ArrayList;

/**
 * Stores all the commands to be compared with for autocorrect function.
 */
public class Dictionary {

    public static final ArrayList<String> commands = new ArrayList<>();

    public static String errorMessage = "Did you mean to use %s?" + "\n" + "Please try using the correct implementation of the command as shown below-";

    public Dictionary() {
        commands.add("add");
        commands.add("check");
        commands.add("clear");
        commands.add("delete");
        commands.add("edit");
        commands.add("shutdown");
        commands.add("lock");
        commands.add("find");
        commands.add("help");
        commands.add("list");
        commands.add("viewall");
        commands.add("view");
    }

    public static String getErrorMessage() {
        return errorMessage;
    }

    public static ArrayList<String> getCommands() {
        return commands;
    }
}
