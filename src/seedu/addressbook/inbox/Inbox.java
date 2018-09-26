package seedu.addressbook.inbox;

import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;
import seedu.addressbook.data.tag.Tag;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Inbox {
    // all messages will be stored here, notifications will appear based on severity and timestamp.
    public static final String COMMAND_WORD = "inbox";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Opens up list of unread notifications. \n\t"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_PROMPT = "Press 'Enter' to take action for Message 1";
    public static int unreadMsgs = 0;
    private String notificationMsg;
    public enum Priority{
        HIGH,   // For messages that require HPQ intervention
        MED,    // For messages that only require PO back-up
        LOW     // Messages that are FYI (e.g. Notifications to admin that details of subjects have changed
    }

    public Inbox(){ // A data structure must be created to store the messages. Use a sortedMap, using <priority, timestamp>
                    // as the key, message as the value. Make preset message templates.

    }

    /** Needs to receive a timestamp from the time the command was executed.
     * Will take in new unread messages.
     * @param message
     * @param pri
     * @param timestamp
     */
    public void addMsg(String message, Priority pri, Timestamp timestamp){

    }

    /** Prints out all unread notifications ordered by priority, then timestamp (earlier first).
     *
     * @return messages to be printed out on the main window.
     */

    /*public String printMsg(){
        //for (String s : messages)
        return
    }*/
}