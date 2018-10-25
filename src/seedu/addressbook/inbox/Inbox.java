//@@author ongweekeong
package seedu.addressbook.inbox;


import java.io.IOException;
import java.util.TreeSet;

import static seedu.addressbook.inbox.Msg.MESSAGE_IS_READ;
import static seedu.addressbook.inbox.Msg.MESSAGE_IS_UNREAD;

public class Inbox {
    // all messages will be stored here, notifications will appear based on severity and timestamp.

    public static final String COMMAND_WORD = "inbox";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Opens up list of unread notifications. \n\t"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_PROMPT = "Press 'Enter' to take action for Message 1";
    public static int numUnreadMsgs = 0;
    private Msg message;

    public static String MESSAGE_STORAGE_FILEPATH;
    private static ReadNotification nw;
    static WriteNotification newMessages;
    static WriteNotification allMessages;

    //protected static HashMap<Triplet<Boolean, Msg.Priority, Timestamp>, Triplet<String, Integer, Location>> notificationsToPrint = new HashMap<>();
    protected static TreeSet<Msg> notificationsToPrint = new TreeSet<>();
    protected static TreeSet<Msg> allNotifications = new TreeSet<>();


    public Inbox(String policeOfficerId) {
        MESSAGE_STORAGE_FILEPATH = MessageFilePaths.getFilePathFromUserId(policeOfficerId);
        nw = new ReadNotification(MESSAGE_STORAGE_FILEPATH);
        newMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH, true);
        allMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH, false);
    }

    public static TreeSet<Msg> loadMsgs() throws IOException {
     //   notificationsToPrint = nw.ReadFromFile();
        notificationsToPrint = nw.ReadFromFile();
        numUnreadMsgs = nw.getNumUnreadMsgs(); // TODO: eventually when bugs are all cleared, use .size() method.
        return notificationsToPrint;
    }

    /** Prints out all unread notifications ordered by priority, then timestamp (earlier first).
     *
     * @return messages to be printed out on the main window.
     */

    public int checkNumUnreadMessages(){
        return numUnreadMsgs;
    }

    /*public static void printNumOfUnreadMsg(){
        if(numUnreadMsgs > 0)
            System.out.println("You have " + numUnreadMsgs + " new message" + ((numUnreadMsgs == 1) ? "." : "s."));
        else
            System.out.println("You have no new messages.");
    }*/
    public String printNumOfUnreadMsg(){
        if(numUnreadMsgs > 0)
            return "You have " + numUnreadMsgs + " new message" + ((numUnreadMsgs == 1) ? "." : "s.");
        else
            return "You have no new messages.";
    }

    /** Do the thing to print out new messages in the specified format.
     * Messages will all be marked as read and rewritten in the new notifications file.
     *
     */
    public static void printNewMsgs() throws IOException {
        // print new messages in order according to TreeSet. After messages are printed, they are considered old.
        Msg myPrintMsg;
        int messageNum = 1;
        // use pollFirstEntry/pollLastEntry to 'pop' the most urgent message to myPrintMsg
        while(!notificationsToPrint.isEmpty()){
            myPrintMsg = notificationsToPrint.pollFirst();
        // Print here. Afterward push back to notificationsToPrint after read status is changed.
            try {
                printMessage(messageNum, myPrintMsg);
            }
            catch(Exception e){
                printMessageNoLocation(messageNum, myPrintMsg);
            }
            messageNum++;
        //  myPrintMsg = markMsgAsRead(myPrintMsg); //TODO: Only markMsgAsUnread when the messages are responded to.
        // put updated message into new TreeSet.
            allNotifications.add(myPrintMsg);
        }
        allMessages.writeToFile(allNotifications); // Overwrites notifications.txt with updated messages.

    }

    public static void printMessage(int messageNum, Msg msgToPrint){
        if(msgToPrint.isRead == MESSAGE_IS_UNREAD) {
            System.out.println(messageNum + " [UNREAD] Priority: " + msgToPrint.getPriority() + ", Sent: " + msgToPrint.getTime() +
                    ", message: " + msgToPrint.getMsg() + ", Coordinates: " + msgToPrint.getLatitude() + ", " +
                    msgToPrint.getLongitude() + ", ETA: " + msgToPrint.getEta() + '.');
        }
        else{
            System.out.println(messageNum + " Priority: " + msgToPrint.getPriority() + ", Sent: " + msgToPrint.getTime() +
                    ", message: " + msgToPrint.getMsg() + ", Coordinates: " + msgToPrint.getLatitude() + ", " +
                    msgToPrint.getLongitude() + ", ETA: " + msgToPrint.getEta() + ".");
        }
    }
    public static void printMessageNoLocation(int messageNum, Msg msgToPrint){
        if(msgToPrint.isRead == MESSAGE_IS_UNREAD) {
            System.out.println(messageNum + " [UNREAD] Priority: " + msgToPrint.getPriority() + ", Sent: " + msgToPrint.getTime() +
                    ", message: " + msgToPrint.getMsg() + '.');
        }
        else{
            System.out.println(messageNum + " Priority: " + msgToPrint.getPriority() + ", Sent: " + msgToPrint.getTime() +
                    ", message: " + msgToPrint.getMsg() + ".");
        }
    }

    public static Msg markMsgAsRead(Msg myMsg){ // Construct a new message, copy from TreeMap, then change read status, update.
        myMsg.isRead = MESSAGE_IS_READ;
        return myMsg;
    }

    public static void main(String[] args) throws IOException {
        /*Msg newMsg = new Msg();
        Location location = new Location(-6.206968,106.751365);
        newMsg.setMsg("Backup requested");
        newMsg.setLocation(location);
        newMsg.setPriority(Msg.Priority.HIGH);
        newMsg.setTime();
        newMessages.writeToFile(newMsg);*/
        loadMsgs();
        //printNumOfUnreadMsg();
        printNewMsgs();
    }
}