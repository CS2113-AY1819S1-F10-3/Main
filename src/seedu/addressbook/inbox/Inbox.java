package seedu.addressbook.inbox;

import org.javatuples.Triplet;

import java.sql.Timestamp;
import java.util.HashMap;

public class Inbox {
    // all messages will be stored here, notifications will appear based on severity and timestamp.
    public static final String MESSAGE_STORAGE_FILEPATH = "notifications.txt";
    public static final String COMMAND_WORD = "inbox";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Opens up list of unread notifications. \n\t"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_PROMPT = "Press 'Enter' to take action for Message 1";
    public static int unreadMsgs = 0;
    private Msg message;
    WriteNotification myMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH);

    protected HashMap<Triplet<Boolean, Priority, Timestamp>, String> notificationsToPrint = new HashMap<Triplet
            <Boolean, Inbox.Priority, Timestamp>, String>();

    public enum Priority{
        HIGH,   // For messages that require HPQ intervention
        MED,    // For messages that only require PO back-up
        LOW     // Messages that are FYI (e.g. Notifications to admin that details of subjects have changed
    }

    public Inbox(){ // A data structure must be created to store the messages from the message storage file.
        Inbox inbox = new Inbox();
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