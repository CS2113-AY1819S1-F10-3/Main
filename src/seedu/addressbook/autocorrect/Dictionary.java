package seedu.addressbook.autocorrect;

import java.util.ArrayList;

/**
 * Stores all the commands to be compared with for autocorrect function.
 */
public class Dictionary {

    public static final ArrayList<String> commands = new ArrayList<>();

    public Dictionary() {
        commands.add("add");
        commands.add("clear");
        commands.add("delete");
        commands.add("exit");
        commands.add("find");
        commands.add("help");
        commands.add("list");
        commands.add("lost");
        commands.add("viewall");
        commands.add("view");
    }

    public static ArrayList<String> getCommands() {
        return commands;
    }
}
