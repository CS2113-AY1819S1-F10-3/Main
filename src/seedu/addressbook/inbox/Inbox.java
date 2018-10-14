package seedu.addressbook.inbox;

import seedu.addressbook.Location;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

public class Inbox {
    // all messages will be stored here, notifications will appear based on severity and timestamp.
    public static final String MESSAGE_STORAGE_FILEPATH = "notifications.txt";
    public static final String COMMAND_WORD = "inbox";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Opens up list of unread notifications. \n\t"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_PROMPT = "Press 'Enter' to take action for Message 1";
    public static int numUnreadMsgs = 0;
    private Msg message;
    private static ReadNotification nw = new ReadNotification(MESSAGE_STORAGE_FILEPATH);
    static WriteNotification newMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH, true);
    static WriteNotification allMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH, false);

    //protected static HashMap<Triplet<Boolean, Msg.Priority, Timestamp>, Triplet<String, Integer, Location>> notificationsToPrint = new HashMap<>();
    protected static TreeSet<Msg> notificationsToPrint = new TreeSet<>();
    protected static TreeSet<Msg> allNotifications = new TreeSet<>();

    public Inbox(){
        Inbox inbox = new Inbox();
    }

    public static void loadMsgs() throws IOException {
     //   notificationsToPrint = nw.ReadFromFile();
        notificationsToPrint = nw.ReadFromFile();
        numUnreadMsgs = nw.getNumUnreadMsgs(); // TODO: eventually when bugs are all cleared, use .size() method.
    }

    /** Prints out all unread notifications ordered by priority, then timestamp (earlier first).
     *
     * @return messages to be printed out on the main window.
     */

    public int checkNumUnreadMessages(HashMap notificationsToPrint){
        return numUnreadMsgs;
    }

    public static void printNumOfUnreadMsg(){ // Set the read status of the messages to true.
        if(numUnreadMsgs > 0)
            System.out.println("You have " + numUnreadMsgs + " new notification" + ((numUnreadMsgs == 1) ? "." : "s."));
        else
            System.out.println("You have no new notifications.");
    }

    /** Do the thing to print out new messages in the specified format.
     * Messages will all be marked as read and rewritten in the new notifications file.
     *
     */
    public static void printNewMsgs() throws IOException { //TODO: GET MY SHIT TOGETHER
        // print new messages in order according to TreeSet. After messages are printed, they are considered old.
        Msg myPrintMsg;
        int messageNum = 1;
        // use pollFirstEntry/pollLastEntry to 'pop' the most urgent message to myPrintMsg
        while(!notificationsToPrint.isEmpty()){
            myPrintMsg = notificationsToPrint.pollFirst(); // TODO: if pollFirst returns null, break out of while loop
        // Print here. Afterward push back to notificationsToPrint after read status is changed.
            try {
                printMessage(messageNum, myPrintMsg);
            }
            catch(Exception e){
                printMessageNoLocation(messageNum, myPrintMsg);
            }
            messageNum++;
            myPrintMsg = markMsgAsRead(myPrintMsg);
        // put updated message into new TreeSet.
            allNotifications.add(myPrintMsg);
        }
        allMessages.writeToFile(allNotifications); // Overwrites notifications.txt with updated messages.

    }

    public static void printMessage(int messageNum, Msg msgToPrint){
        System.out.println(messageNum + " Priority: " + msgToPrint.getPriority() + ", Sent: " + msgToPrint.getTime() +
                ", message: " + msgToPrint.getMsg() + ", Coordinates: " + msgToPrint.getLatitude() + ", " +
                msgToPrint.getLongitude() + ", ETA: " + msgToPrint.getEta() + '.');
    }
    public static void printMessageNoLocation(int messageNum, Msg msgToPrint){
        System.out.println(messageNum + " Priority: " + msgToPrint.getPriority() + ", Sent: " + msgToPrint.getTime() +
                ", message: " + msgToPrint.getMsg() + '.');
    }

    public static Msg markMsgAsRead(Msg myMsg){ // Construct a new message, copy from TreeMap, then change read status, update.
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
        /*loadMsgs();
        printNumOfUnreadMsg();
        printNewMsgs();*/
    }
}