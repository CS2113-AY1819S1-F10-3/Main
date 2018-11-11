package seedu.addressbook.commands;

import seedu.addressbook.autocorrect.CheckDistance;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.person.NRIC;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;

/**
 * Deletes a person identified using the NRIC parameter.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    //@@author muhdharun -reused

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Deletes the person by nric.\n\t"
            + "Parameters: NRIC\n\t"
            + "Example: " + COMMAND_WORD + " s1234567a";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private NRIC toDelete;

    public DeleteCommand(NRIC nric) {
        this.toDelete = nric;
    }


    @Override
    public CommandResult execute() {
        try {
            final ReadOnlyPerson target = (toDelete == null) ? getTargetPerson() : getTargetPerson(toDelete);
            addressBook.removePerson(target);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, target));
        } catch (PersonNotFoundException pnfe) {
            //@@author ShreyasKp
            CheckDistance checker = new CheckDistance();

            String nric = toDelete.toString();
            String prediction = checker.checkInputDistance(nric);

            if (!prediction.equals("none")) {
                return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK
                        + "\n"
                        + "Did you mean to use "
                        + prediction);
            } else {
                return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
            }
        }
    }

}
