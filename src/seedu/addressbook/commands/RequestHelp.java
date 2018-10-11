package seedu.addressbook.commands;

import seedu.addressbook.data.exception.IllegalValueException;
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






    public static String MESSAGE_REQUEST_SUCCESS = "Backup request success!";


    public RequestHelp(String caseName, String messageString) {
        WriteNotification writeNotification = new WriteNotification("requestList.txt", true);

        try {
            Msg requestHelpMessage = new Msg(Offense.getPriority(caseName), messageString); // TODO:
            writeNotification.writeToFile(requestHelpMessage);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
