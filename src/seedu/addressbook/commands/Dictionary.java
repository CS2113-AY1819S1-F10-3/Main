//@@author ShreyasKp
package seedu.addressbook.commands;

import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.storage.StorageFile;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stores all the commands to be compared with for autocorrect function.
 */
public class Dictionary extends Command{

    public static final ArrayList<String> commands = new ArrayList<>();

    public String errorMessage = "Did you mean to use %s?" + "\n" + "Please try using the correct implementation of the input as shown below-";

    public String errorCommandMessage = "Did you mean to use %s?" + "\n" + "Please try using the correct implementation of the command as shown below-";

    public ArrayList<String> details = new ArrayList<>();
    private AddressBook addressBook;

    public Dictionary() {
        try {
            StorageFile storage = new StorageFile();
            this.addressBook = storage.load();
        } catch(Exception e) {

        } finally {
            commands.add(AddCommand.COMMAND_WORD);
            commands.add(CheckCommand.COMMAND_WORD);
            commands.add(CheckPOStatusCommand.COMMAND_WORD);
            commands.add(ClearCommand.COMMAND_WORD);
            commands.add(DateTimeCommand.COMMAND_WORD);
            commands.add(DeleteCommand.COMMAND_WORD);
            commands.add(DispatchCommand.COMMAND_WORD);
            commands.add(EditCommand.COMMAND_WORD);
            commands.add(ExitCommand.COMMAND_WORD);
            commands.add(FindCommand.COMMAND_WORD);
            commands.add(HelpCommand.COMMAND_WORD);
            commands.add(InboxCommand.COMMAND_WORD);
            commands.add(ClearInboxCommand.COMMAND_WORD);
            commands.add(ListCommand.COMMAND_WORD);
            commands.add(LockCommand.COMMAND_WORD);
            commands.add(ReadCommand.COMMAND_WORD);
            commands.add(RequestHelpCommand.COMMAND_WORD);
            commands.add(UpdateStatusCommand.COMMAND_WORD);
            //commands.add(UpdatePasswordCommand.COMMAND_WORD);
            commands.add(ViewAllCommand.COMMAND_WORD);
        }


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

    public ArrayList<String> getDetails() {
        try {


            for (ReadOnlyPerson person : addressBook.getAllPersons().immutableListView()) {
                details.add(person.getNric().getIdentificationNumber());
            }
            return details;
        }
        catch(NullPointerException e) {
            ArrayList<String> x = new ArrayList<>();
            x.addAll(new ArrayList<>(Arrays.asList("Empty")));
            return x;
        }
    }
}
