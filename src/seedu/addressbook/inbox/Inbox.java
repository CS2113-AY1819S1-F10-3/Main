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
    static WriteNotification myMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH, true);

    protected static HashMap<Triplet<Boolean, Msg.Priority, Timestamp>, Triplet<String, Integer, Location>> notificationsToPrint = new HashMap<>();


    public Inbox(){
        Inbox inbox = new Inbox();
    }

    public static void loadMsgs() throws IOException {
        notificationsToPrint = nw.ReadFromFile();
        unreadMsgs = nw.getUnreadMsgs();
    }

    /** Prints out all unread notifications ordered by priority, then timestamp (earlier first).
     *
     * @return messages to be printed out on the main window.
     */

    public int checkNumUnreadMessages(HashMap notificationsToPrint){

        return unreadMsgs;
    }

    public static void printMsg(){
        if(unreadMsgs > 0)
            System.out.println("You have " + unreadMsgs + " unread message" + ((unreadMsgs == 1) ? "." : "s."));
        else System.out.println(unreadMsgs);

    }

    public static void main(String[] args) throws IOException {
        /*Msg newMsg = new Msg();
        Location location = new Location(-6.206968,106.751365);
        newMsg.addMsg("Backup requested");
        newMsg.setLocation(location);
        newMsg.setPriority(Msg.Priority.HIGH);
        newMsg.setTime();
        myMessages.writeToFile(newMsg);*/
        loadMsgs();
        printMsg();
    }
}