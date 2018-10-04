package seedu.addressbook.inbox;

import org.javatuples.Triplet;
import seedu.addressbook.Location;

import java.io.IOException;
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
    private static ReadNotification nw = new ReadNotification(MESSAGE_STORAGE_FILEPATH);
    WriteNotification myMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH, true);

    protected HashMap<Triplet<Boolean, Msg.Priority, Timestamp>, Triplet<String, Integer, Location>> notificationsToPrint = new HashMap<>();


    public Inbox(){
        Inbox inbox = new Inbox();
    }

    public void loadMessages() throws IOException {
        notificationsToPrint = nw.ReadFromFile();
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