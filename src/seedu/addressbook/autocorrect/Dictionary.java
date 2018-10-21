package seedu.addressbook.autocorrect;

import seedu.addressbook.commands.*;
import seedu.addressbook.data.person.ReadOnlyPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all the commands to be compared with for autocorrect function.
 */
public class Dictionary {

    public static final ArrayList<String> commands = new ArrayList<>();

    public String errorMessage = "Did you mean to use %s?" + "\n" + "Please try using the correct implementation of the input as shown below-";

    public String errorCommandMessage = "Did you mean to use %s?" + "\n" + "Please try using the correct implementation of the command as shown below-";

    //List<ReadOnlyPerson> allPersons = addressBook.getAllPersons().immutableListView();

    public Dictionary() {
        commands.add(AddCommand.COMMAND_WORD);
        commands.add(CheckCommand.COMMAND_WORD);
        commands.add(ClearCommand.COMMAND_WORD);
        commands.add(DeleteCommand.COMMAND_WORD);
        commands.add(EditCommand.COMMAND_WORD);
        commands.add(ExitCommand.COMMAND_WORD);
        commands.add(FindCommand.COMMAND_WORD);
        commands.add(HelpCommand.COMMAND_WORD);
        commands.add(InboxCommand.COMMAND_WORD);
        commands.add(ListCommand.COMMAND_WORD);
        commands.add(LockCommand.COMMAND_WORD);
        commands.add(RequestHelp.COMMAND_WORD);
        commands.add(UpdatePasswordCommand.COMMAND_WORD);
        commands.add(ViewAllCommand.COMMAND_WORD);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getCommandErrorMessage() {
        return errorCommandMessage;
    }

    public static ArrayList<String> getCommands() {

        return commands;
    }
}
