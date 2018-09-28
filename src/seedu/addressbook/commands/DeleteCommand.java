package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;


/**
 * Deletes a person identified using it's last displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" 
            + "Deletes the person by nric.\n\t"
            + "Parameters: NRIC\n\t"
            + "Example: " + COMMAND_WORD + " s1234567";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private String nameToSearch = "";

    public DeleteCommand(int targetVisibleIndex) {
        super(targetVisibleIndex);
    }

    public DeleteCommand(String name) {
        this.nameToSearch = name;
    }


    @Override
    public CommandResult execute() {
        try {
            final ReadOnlyPerson target = (nameToSearch.isEmpty()) ? getTargetPerson() : getTargetPerson(nameToSearch);
            addressBook.removePerson(target);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, target));
        } catch (IndexOutOfBoundsException ie) {
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } catch (PersonNotFoundException pnfe) {
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        }
    }

}
