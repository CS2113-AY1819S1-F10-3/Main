package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.inbox.Inbox;
import seedu.addressbook.inbox.Msg;

import java.io.IOException;
import java.util.TreeSet;

public class InboxCommand extends Command {
    public static final String COMMAND_WORD = "showunread";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Displays all unread messages in the application starting from the most urgent.\n\t"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        /*List<ReadOnlyPerson> allPersons = addressBook.getAllPersons().immutableListView();
        return new CommandResult(getMessageForPersonListShownSummary(allPersons), allPersons);*/
        Inbox myInbox = new Inbox();
        TreeSet<Msg> allMsgs = null; // TODO: Figure out how to send the whole list of messages over.
        int myUnreadMsgs;
        int messageNum = 1;
        Msg msgToPrint;
        try {
            allMsgs = myInbox.loadMsgs();
            myUnreadMsgs = myInbox.checkNumUnreadMessages();
            if(myUnreadMsgs!=0) {
                String fullPrintedMessage = Messages.MESSAGE_UNREAD_MSG_NOTIFICATION + '\n';
                for(int i=0; i<myUnreadMsgs; i++){
                    msgToPrint = allMsgs.pollFirst();
                    assert msgToPrint != null;
                    fullPrintedMessage += String.valueOf(messageNum++) + ".\t[UNREAD] Priority: " + msgToPrint.getPriority() + ", Sent: " +
                            msgToPrint.getTime() + ", message: " + msgToPrint.getMsg() + ", Coordinates: " + msgToPrint.getLatitude() + ", " +
                            msgToPrint.getLongitude() + ", ETA: " + msgToPrint.getEta() + ".\n";
                }
                return new CommandResult(String.format(fullPrintedMessage, myUnreadMsgs));
            }
            else{
                return new CommandResult(Messages.MESSAGE_NO_UNREAD_MSGS);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new CommandResult("Error loading messages.");
        }
    }
}
