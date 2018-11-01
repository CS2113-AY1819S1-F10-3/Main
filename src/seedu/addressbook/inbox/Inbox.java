//@@author ongweekeong
package seedu.addressbook.inbox;


import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

public class Inbox {
    // all messages will be stored here, notifications will appear based on severity and timestamp.
    public static String MESSAGE_STORAGE_FILEPATH;
    public static final String INBOX_NOT_READ_YET = "You have not read your inbox! \n\t" +
            "Type \"showunread\" to view your unread messages.";
    public static final String INBOX_NO_UNREAD_MESSAGES = "You have no unread messages in your inbox.";
    public static final String INDEX_OUT_OF_BOUNDS = "Index entered is out of bounds. Enter message number from 1 to %1$d.";
    public static final String MESSAGE_STORAGE_PATH_NOT_FOUND = "Cannot find file to write to.";
    public static final String MESSAGE_READ_STATUS_UPDATED = "Successful update";
    public static int numUnreadMsgs = -1;
    protected static TreeSet<Msg> notificationsToPrint = new TreeSet<>();
    protected static HashMap<Integer, Msg> recordNotifications = new HashMap<>();
    protected static ReadNotification readNotification;
    protected static WriteNotification allMessages;
    int messageIndex = 1;

    static WriteNotification newMessages;


    public Inbox(String policeOfficerId) {
        MESSAGE_STORAGE_FILEPATH = MessageFilePaths.getFilePathFromUserId(policeOfficerId);
        readNotification = new ReadNotification(MESSAGE_STORAGE_FILEPATH);
        newMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH, true);
        allMessages = new WriteNotification(MESSAGE_STORAGE_FILEPATH, false);
    }

    public TreeSet<Msg> loadMsgs() throws IOException {
        notificationsToPrint = readNotification.ReadFromFile();
        messageIndex = 1;
        for (Msg message : notificationsToPrint){
            recordNotifications.put(messageIndex++, message);
        }
        numUnreadMsgs = readNotification.getNumUnreadMsgs();
        return notificationsToPrint;
    }

    public String markMsgAsRead(int index) throws NullPointerException, IndexOutOfBoundsException {
        try{
            if((index < 1) || (index > numUnreadMsgs)){
                throw new IndexOutOfBoundsException();
            }
            recordNotifications.get(index).setMsgAsRead();
            for(int i = 1; i <= recordNotifications.size(); i++) {
                notificationsToPrint.add(recordNotifications.get(i));
            }
            allMessages.writeToFile(notificationsToPrint);
        }
        catch (IndexOutOfBoundsException e){
            if(numUnreadMsgs>0) {
                return INDEX_OUT_OF_BOUNDS;
            }
            else if(numUnreadMsgs == -1){
                return INBOX_NOT_READ_YET;
            }
            else if(numUnreadMsgs == 0) {
                return INBOX_NO_UNREAD_MESSAGES;
            }
        }
        catch (IOException e){
            return MESSAGE_STORAGE_PATH_NOT_FOUND;
        }
        return MESSAGE_READ_STATUS_UPDATED;
    }

    public int checkNumUnreadMessages(){
        return numUnreadMsgs;
    }

}