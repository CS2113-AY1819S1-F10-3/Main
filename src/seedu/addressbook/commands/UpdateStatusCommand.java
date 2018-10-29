package seedu.addressbook.commands;

//@@author muhdharun

import seedu.addressbook.PatrolResourceStatus;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;

/**
 * Command to update the status of a PO to 'free' (isEngaged set to false)
 */

public class UpdateStatusCommand extends Command {

    public static final String COMMAND_WORD = "updatestatus";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Updates the 'isEngaged' status of PO to false \n\t"
            + "Parameters: po(ID)\n\t"
            + "Example: " + COMMAND_WORD + " po2";


    private String toUpdate;

    public static final String MESSAGE_UPDATE_PO_SUCCESS = "%s is now free (not engaged)";

    public UpdateStatusCommand(String po) {this.toUpdate = po;}

    @Override
    public CommandResult execute() {
        try {
            PatrolResourceStatus.setStatus(toUpdate,false);
            return new CommandResult(String.format(MESSAGE_UPDATE_PO_SUCCESS,toUpdate));
        } catch (IllegalValueException e) {
            return new CommandResult(String.format(Messages.MESSAGE_PO_NOT_FOUND));
        }
    }

}
