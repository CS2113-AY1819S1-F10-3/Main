//@@author ongweekeong
package seedu.addressbook.commands;

import seedu.addressbook.inbox.Inbox;
import seedu.addressbook.password.Password;




/**
 * Updates read status of message identified using it's last displayed index from the user's inbox.
 */

public class ReadCommand extends Command {

    public static final String COMMAND_WORD = "read";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Updates the message read status by index \n\t"
            + "Parameters: Index\n\t"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UPDATE_SUCCESS = "Message mark as read!";

    private int index;

    public ReadCommand(int targetVisibleIndex) {
        this.index = targetVisibleIndex;
    }

    public Inbox myInbox = new Inbox(Password.getID());

    //TODO: Decide if Inbox data structures should be set to static? since that will allow me to access those data structures from other inbox objects in another class
    @Override
    public CommandResult execute() {
        String result = myInbox.markMsgAsRead(index);
        if(result.equals(myInbox.MESSAGE_READ_STATUS_UPDATED)) {
            return new CommandResult(MESSAGE_UPDATE_SUCCESS);
        }
        else if(result.equals(myInbox.INDEX_OUT_OF_BOUNDS)){
            return new CommandResult(String.format(myInbox.INDEX_OUT_OF_BOUNDS, myInbox.checkNumUnreadMessages()));
        }
        else if(result.equals(myInbox.INBOX_NOT_READ_YET)){
            return new CommandResult(myInbox.INBOX_NOT_READ_YET);
        }
        else if(result.equals(myInbox.INBOX_NO_UNREAD_MESSAGES)){
            return new CommandResult(myInbox.INBOX_NO_UNREAD_MESSAGES);
        }
        else{
            return new CommandResult("Unknown error in updating messages.");
        }
    }

}
