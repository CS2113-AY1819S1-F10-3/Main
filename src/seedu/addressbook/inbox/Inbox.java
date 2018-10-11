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
    static WriteNotification newMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH, true);
    static WriteNotification oldMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH, false);

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

    public static void printNumOfUnreadMsg(){ // Set the read status of the messages to true.
        if(unreadMsgs > 0)
            System.out.println("You have " + unreadMsgs + " new notification" + ((unreadMsgs == 1) ? "." : "s."));
        else
            System.out.println("You have no new notifications.");
    }

    /** Do the thing to print out new messages in the specified format.
     * Messages will all be marked as read and rewritten in the new notifications file.
     *
     */
    public void printNewMsgs(){
        // print new messages in order according to TreeMap. After messages are printed, they are considered old.
        Msg myPrintMsg = new Msg();
        // use pollFirstEntry/pollLastEntry to 'pop' the most urgent message to myPrintMsg

        // After

        myPrintMsg = markMsgasRead(myPrintMsg);
        // use put to associate updated message into TreeMap.
        notificationsToPrint.put(Triplet.with(myPrintMsg.isRead, myPrintMsg.getPriority(), myPrintMsg.getTime()),
                Triplet.with(myPrintMsg.getMsg(), myPrintMsg.getEta(), myPrintMsg.getLocation()));
        // After the whole process is done, rewrite notifications into notifications.txt.

    }

    public Msg markMsgasRead(Msg myMsg){ // Construct a new message, copy from TreeMap, then change read status, update.
        myMsg.isRead = Msg.MESSAGE_IS_READ;
        return myMsg;
    }

    public static void main(String[] args) throws IOException {
        Msg newMsg = new Msg();
        Location location = new Location(-6.206968,106.751365);
        newMsg.addMsg("Backup requested");
        newMsg.setLocation(location);
        newMsg.setPriority(Msg.Priority.HIGH);
        newMsg.setTime();
        newMessages.writeToFile(newMsg);
        loadMsgs();
        printNumOfUnreadMsg();
    }
}