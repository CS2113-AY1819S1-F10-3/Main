package seedu.addressbook.commands;

//@@author muhdharun

import seedu.addressbook.PatrolResourceStatus;

import static seedu.addressbook.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class UpdateStatusCommand extends Command {

    public static final String COMMAND_WORD = "updatestatus";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Updates the 'isEngaged' status of PO to false \n\t"
            + "Parameters: PO ID\n\t"
            + "Example: " + COMMAND_WORD + " po2";

    private String toUpdate;

    public static final String MESSAGE_UPDATE_PO_SUCCESS = "%s is now free (not engaged)";

    public UpdateStatusCommand(String po) {this.toUpdate = po;}

    @Override
    public CommandResult execute() {
        PatrolResourceStatus.setStatus(toUpdate,false);

        return new CommandResult(String.format(MESSAGE_UPDATE_PO_SUCCESS,toUpdate));
    }

}
