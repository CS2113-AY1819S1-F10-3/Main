package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.Offense;
import seedu.addressbook.inbox.Inbox;
import seedu.addressbook.inbox.Msg;
import seedu.addressbook.inbox.WriteNotification;

import java.io.IOException;
import java.util.ArrayList;

public class RequestHelp extends Command {
    public static final String COMMAND_WORD = "request";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Requests help from headquarters.\n\t"
            + "Message from police officer can be appended in text.\n\t"
            + "Example: " + COMMAND_WORD
            + " GUN \t"
            + " Help needed on Jane Street";


    public static String MESSAGE_REQUEST_SUCCESS = "Request for backup is successful.";

    private Msg requestHelpMessage;
    private WriteNotification writeNotification;

    public RequestHelp(String caseName, String messageString) throws IllegalValueException {
        writeNotification = new WriteNotification("requestList.txt", true);
        requestHelpMessage = new Msg(Offense.getPriority(caseName), messageString);
    }


    @Override
    public CommandResult execute() {
        try {
            writeNotification.writeToFile(requestHelpMessage);
            return new CommandResult(String.format(MESSAGE_REQUEST_SUCCESS));
        } catch (IOException ioe) {
            return new CommandResult(Messages.MESSAGE_SAVE_ERROR);
        }
    }


}
