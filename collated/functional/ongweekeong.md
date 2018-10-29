# ongweekeong
###### \seedu\addressbook\commands\InboxCommand.java
``` java
package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.inbox.Inbox;
import seedu.addressbook.inbox.Msg;
import seedu.addressbook.password.Password;
import seedu.addressbook.timeanddate.TimeAndDate;

import java.io.IOException;
import java.util.TreeSet;

public class InboxCommand extends Command {
    public static final String COMMAND_WORD = "showunread";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Displays all unread messages in the application starting from the most urgent.\n\t"
            + "Example: " + COMMAND_WORD;


    @Override
    public CommandResult execute() {

        Inbox myInbox = new Inbox(Password.getID());
        TreeSet<Msg> allMsgs;
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
                    fullPrintedMessage += concatenateMsg(messageNum, msgToPrint);
                    messageNum++;
                }
                allMsgs.clear();
                return new CommandResult(String.format(fullPrintedMessage, myUnreadMsgs));
            }
            else{
                allMsgs.clear();
                return new CommandResult(Messages.MESSAGE_NO_UNREAD_MSGS);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new CommandResult("Error loading messages.");
        }
    }

    public static String concatenateMsg(int messageNum, Msg message) throws NullPointerException{
        String concatenatedMsg;
        TimeAndDate dateFormatter = new TimeAndDate();
        try{
            concatenatedMsg = String.valueOf(messageNum) + ".\t[UNREAD] Sender: " + message.getSenderId() + " Priority: " + message.getPriority() +
                    ", Sent: " + dateFormatter.outputDATHrs(message.getTime()) + ",\n\t\tMessage: " + message.getMsg() + ", Coordinates: " +
                    message.getLatitude() + ", " + message.getLongitude() + ", ETA: " + message.getEta() + ".\n";
        }
        catch(Exception e){
            concatenatedMsg = String.valueOf(messageNum) + ".\t[UNREAD] Sender: " + message.getSenderId() + " Priority: " +
                    message.getPriority() + ", Sent: " + dateFormatter.outputDATHrs(message.getTime()) + ",\n\t\tMessage: " + message.getMsg() + ".\n";
        }
        return concatenatedMsg;
    }

}
```
###### \seedu\addressbook\commands\ReadCommand.java
``` java
package seedu.addressbook.commands;

import seedu.addressbook.inbox.Inbox;
import seedu.addressbook.password.Password;




/**
 * Updates read status of message identified using it's last displayed index from the user's inbox.
 */

public class ReadCommand extends Command {

    public static final String COMMAND_WORD = "read";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Marks message as read by index displayed after \"showunread\" command is used. \n\t"
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
```
###### \seedu\addressbook\inbox\Inbox.java
``` java
package seedu.addressbook.inbox;


import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import static seedu.addressbook.inbox.Msg.MESSAGE_IS_READ;
import static seedu.addressbook.inbox.Msg.MESSAGE_IS_UNREAD;

public class Inbox {
    // all messages will be stored here, notifications will appear based on severity and timestamp.
    public static String MESSAGE_STORAGE_FILEPATH;
    public static final String INBOX_NOT_READ_YET = "You have not read your inbox! \n\t" +
            "Type \"showunread\" to view your unread messages.";
    public static final String INBOX_NO_UNREAD_MESSAGES = "You have no unread messages in your inbox.";
    public static final String INDEX_OUT_OF_BOUNDS = "Index entered is out of bounds. Enter message number from 1 to %1$d.";
    public static final String MESSAGE_STORAGE_PATH_NOT_FOUND = "Cannot find file to write to.";
    public static final String MESSAGE_READ_STATUS_UPDATED = "Successful update";
    //public static final String COMMAND_WORD = "inbox";
    //public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Opens up list of unread notifications. \n\t"
    //        + "Example: " + COMMAND_WORD;
    //public static final String MESSAGE_PROMPT = "Press 'Enter' to take action for Message 1";
    public static int numUnreadMsgs = -1;
    protected static TreeSet<Msg> notificationsToPrint = new TreeSet<>();
    protected static HashMap<Integer, Msg> recordNotifications = new HashMap<>();
    protected static ReadNotification readNotification;
    protected static WriteNotification allMessages; //TODO: Overwrite read status of messages after action taken
    int messageIndex = 1;

    static WriteNotification newMessages;
    protected static TreeSet<Msg> allNotifications = new TreeSet<>();


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
//            notificationsToPrint.remove(recordNotifications.get(index));
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

    /*public static void main(String[] args) throws IOException {
        /*Msg newMsg = new Msg();
        Location location = new Location(-6.206968,106.751365);
        newMsg.setMsg("Backup requested");
        newMsg.setLocation(location);
        newMsg.setPriority(Msg.Priority.HIGH);
        newMsg.setTime();
        newMessages.writeToFile(newMsg);
        loadMsgs();
        //printNumOfUnreadMsg();
        printNewMsgs();
    }*/
}
```
###### \seedu\addressbook\inbox\MessageFilePaths.java
``` java
package seedu.addressbook.inbox;

/**
 * Container for filepaths to storage files of each user's messages.
 */
public class MessageFilePaths {
    public static final String FILEPATH_HQP_INBOX = "inboxMessages/headquartersInbox";
    public static final String FILEPATH_PO1_INBOX = "inboxMessages/PO1";
    public static final String FILEPATH_PO2_INBOX = "inboxMessages/PO2";
    public static final String FILEPATH_PO3_INBOX = "inboxMessages/PO3";
    public static final String FILEPATH_PO4_INBOX = "inboxMessages/PO4";
    public static final String FILEPATH_PO5_INBOX = "inboxMessages/PO5";
    public static final String FILEPATH_DEFAULT = "notifications.txt";

    public static String getFilePathFromUserId(String userId){
        switch(userId) {

            case "hqp":
                return MessageFilePaths.FILEPATH_HQP_INBOX;
            case "po1":
                return MessageFilePaths.FILEPATH_PO1_INBOX;
            case "po2":
                return MessageFilePaths.FILEPATH_PO2_INBOX;
            case "po3":
                return MessageFilePaths.FILEPATH_PO3_INBOX;
            case "po4":
                return MessageFilePaths.FILEPATH_PO4_INBOX;
            case "po5":
                return MessageFilePaths.FILEPATH_PO5_INBOX;
            default:
                return FILEPATH_DEFAULT;
        }
    }

}
```
###### \seedu\addressbook\inbox\Msg.java
``` java
package seedu.addressbook.inbox;


import seedu.addressbook.common.Location;
import seedu.addressbook.password.Password;
import seedu.addressbook.timeanddate.TimeAndDate;

import java.sql.Timestamp;

/** Msg has the following attributes:
 *  @params Read Status, Priority, timestamp, message, location (x,y coordinates) and ETA.
 */


public class Msg implements Comparable <Msg> {
    private String senderId = Password.getID();
    protected String receiverId;
    private String newMsg;
    private Priority priority;
    private Location location;
    private int eta = -1;

    //private String comment;
    protected boolean isRead;
    protected boolean isLocationAvailable;
    private Timestamp time;
    public static final boolean MESSAGE_IS_READ = true;
    public static final boolean MESSAGE_IS_UNREAD = false;



    public enum Priority {
        HIGH(3),   // For messages that require HPQ intervention
        MED(2),    // For messages that only require PO back-up
        LOW(1);     // Messages that are FYI (e.g. Notifications to admin that details of subjects have changed

        private int priority;

        Priority (int priority) {
            this.priority = priority;
        }

        public int toInteger() {
            return priority;
        }

    }

    public Msg(){
        isLocationAvailable = false;
        isRead = MESSAGE_IS_UNREAD;
    }

    // constructor for dispatcher message

    // constructor for requester message
    

    public Msg(Priority urgency, String message){
        isLocationAvailable = false;
        isRead = MESSAGE_IS_UNREAD;
        priority = urgency;
        time = new Timestamp(System.currentTimeMillis());
        newMsg = message;
    }

    public Msg(Priority urgency, String message, Location myLocale){ // Constructor for request backup message
        isLocationAvailable = true;
        isRead = MESSAGE_IS_UNREAD;
        priority = urgency;
        time = new Timestamp(System.currentTimeMillis());
        newMsg = message;
        location = myLocale;
    }

    public Msg(Priority urgency, String message, Location requesterLocale, int myEta){ // Constructor for request backup message
        isLocationAvailable = true;                                                    // An ack message should be sent to PO requesting
        isRead = MESSAGE_IS_UNREAD;                                                    // backup, stating ETA of backup deployed.
        priority = urgency;
        time = new Timestamp(System.currentTimeMillis());
        newMsg = message;
        location = requesterLocale;
        eta = myEta;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId(){
        return this.senderId;
    }

    public void setMsg(String msg){
        this.newMsg = msg;
    }

    public void setPriority(Priority urgency){
        this.priority = urgency;
    }

    public Priority getPriority(){
        return this.priority;
    }

    public String getMsg(){
        return this.newMsg;
    }

    public void setLocation(Location place){
        this.location = place;
        isLocationAvailable = true;
    }

    public Location getLocation(){
        return location;
    }

    public void setLongitude(double x){
        location.setLongitude(x);
    }

    public void setLatitude(double y){
        location.setLatitude(y);
    }

    public void setMsgAsRead(){
        this.isRead = MESSAGE_IS_READ;
    }

    public void setMsgAsUnread(){
        this.isRead = MESSAGE_IS_UNREAD;
    }

    public double getLongitude(){
        return location.getLongitude();
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public void setEta(int eta){
        this.eta = eta;
    }

    public int getEta(){
        return this.eta;
    }

    public boolean hasEta(){
       return eta != -1;
    }

//    public String getPoliceOfficerId() {
//        return (this.policeOfficerId == null) ? "-" : policeOfficerId;
//    }
//    public void setPoliceOfficerId(String policeOfficerId) { this.policeOfficerId = policeOfficerId; }
//
//    public boolean hasPoliceOfficerId() {
//        return policeOfficerId != null;
//    }

    public void setTime(){
        time = new Timestamp(System.currentTimeMillis()); // Set to current time if no timestamp passed.
    }

    public void setTime(Timestamp time){
        this.time = time;
    }

    public Timestamp getTime(){
        return this.time;
    }

    public String getTimeString(){
        return TimeAndDate.outputDATHrs(this.time);
    }

    @Override
    public int compareTo(Msg other) {
        int otherInt = other.isRead? 1 : 0;
        int myInt = this.isRead? 1 : 0;
        int compare = Integer.compare(myInt, otherInt);
        if(compare == 0){ // If same read status, compare priorities.
            compare = compareByPriority(other);
            if(compare == 0){ // If priority is the same, compare by timestamp.
                compare = compareByTimestamp(other);
            }
            return compare;
        }
        else{
            return compare;
        }
    }

    // TODO:
    public int compareByPriority(Msg other){
        return Integer.compare(other.getPriority().toInteger(), this.getPriority().toInteger());
    }

    public int compareByTimestamp(Msg other){
        return this.getTime().compareTo(other.getTime());
    }

}
```
###### \seedu\addressbook\inbox\ReadNotification.java
``` java
package seedu.addressbook.inbox;

import seedu.addressbook.common.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

public class ReadNotification {
    private static String path;
    private Msg returnMsg;
    private int unreadMsgs = 0;
    protected static TreeSet<Msg> sortedMsgs = new TreeSet<>();

    public ReadNotification(String filePath) {
        path = filePath;
    }


    public TreeSet<Msg>
    ReadFromFile() throws IOException {  // If no new notifications and 'inbox' command invoked, show past 10 notifications
        String line;
        BufferedReader br = new BufferedReader(new FileReader(path));
        unreadMsgs = 0;
        while ((line = br.readLine()) != null) {
            if (line.equals("> START OF MESSAGE <")) {
                returnMsg = new Msg();
            }
            else if (line.equals("> END OF MESSAGE <")) {  // End of message entry, store into TreeSet
                sortedMsgs.add(returnMsg);
            }
            else {
                String[] parts = line.split(":", 2);
                String msgType = parts[0];
                if (parts.length == 2) {
                    if(msgType.equals("Sender ID")){
                        readMsgSenderId(parts[1]);
                    }
                    else if (msgType.equals("Read status")) {
                        readMsgReadStatus(parts[1]);
                        if (!returnMsg.isRead)
                            unreadMsgs += 1;
                    }
                    else if (msgType.equals("Priority")) {
                        readMsgPriority(parts[1]);
                    }
                    else if (msgType.equals("Timestamp")) {
                        readMsgTimestamp(parts[1]);
                    }
                    else if (msgType.equals("Message")) {
                        readMsgMessage(parts[1]);
                    }
                    else if (msgType.equals("ETA")) {
                        readMsgEta(parts[1]);
                    }
                    else if (msgType.equals("Location")) {
                        readMsgLocation(parts[1]);
                    }
//                    else if (msgType.equals("Police Officer ID")) {
//                        readMsgPO(parts[1]);
//                    }
                }

            }
        }
        return sortedMsgs;
    }

    public int getNumUnreadMsgs(){
        return this.unreadMsgs;
    }

    public void resetUnreadMsgs(){
        this.unreadMsgs = 0;
    }

    public void readMsgSenderId(String userId){
        this.returnMsg.setSenderId(userId);
    }

    public void readMsgReadStatus(String readStatus) {
        this.returnMsg.isRead = Boolean.parseBoolean(readStatus);
    }

    public void readMsgPriority(String priority){
        Msg.Priority msgPriority;
        if (priority.equals("HIGH"))
            msgPriority = Msg.Priority.HIGH;
        else if (priority.equals("MED")) {
            msgPriority = Msg.Priority.MED;
        }
        else {
            msgPriority = Msg.Priority.LOW;
        }

        this.returnMsg.setPriority(msgPriority);
    }


    public void readMsgTimestamp(String timestamp){
        SimpleDateFormat timeFormatted = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss:SSS");
        Date parsedTimeStamp = new Date();
        try {
            parsedTimeStamp = timeFormatted.parse(timestamp);
        } catch (ParseException e) {
            // Find a way to debug this if time format is invalid.
        }
        Timestamp msgTime = new Timestamp(parsedTimeStamp.getTime());
        returnMsg.setTime(msgTime);
    }

    public void readMsgMessage(String message){
        this.returnMsg.setMsg(message);
    }

    public void readMsgEta (String eta){
        this.returnMsg.setEta(Integer.parseInt(eta));
    }

    public void readMsgLocation(String xyValue){
        String[] coordinates = xyValue.split(",", 2);
        Location myLocation = new Location(Double.parseDouble(coordinates[0]),
                Double.parseDouble(coordinates[1]));
        this.returnMsg.setLocation(myLocation);
    }
//
//    public void readMsgPO(String poId) {
//        this.returnMsg.setPoliceOfficerId(poId);
//    }

}
```
###### \seedu\addressbook\inbox\WriteNotification.java
``` java
package seedu.addressbook.inbox;

import seedu.addressbook.timeanddate.TimeAndDate;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeSet;

public class WriteNotification {
    private String path;
    private boolean isAppend = false;

    public WriteNotification(String userId){
        path = MessageFilePaths.getFilePathFromUserId(userId);
        this.isAppend = true;
    }

    public WriteNotification(String filePath, boolean appendValue){
        path = filePath;
        isAppend = appendValue;
    }


    /**	Message format should look like this
     *	Read/Unread (1 or 0)
     *	Priority of Message
     *	Timestamp of message
     *	Message
     *  ETA, if applicable
     *  Location, if available
     */

    public void writeToFile(Msg message) throws IOException{
        TimeAndDate dateFormatter = new TimeAndDate();
        FileWriter write = new FileWriter (path, isAppend);
        PrintWriter myPrinter = new PrintWriter(write);
        myPrinter.println("> START OF MESSAGE <");
        myPrinter.println("Sender ID:" + message.getSenderId());
        myPrinter.println("Read status:" + message.isRead);
        myPrinter.println("Priority:" + message.getPriority());
        myPrinter.println("Timestamp:" + dateFormatter.outputDATHrs());
        myPrinter.println("Message:" + message.getMsg());

        if(message.hasEta())
            myPrinter.println("ETA:" + message.getEta());
        else myPrinter.println('-');

        if(message.isLocationAvailable) {
            myPrinter.println("Location:" + message.getLatitude() + "," + message.getLongitude());
        }
        else myPrinter.println('-');

        myPrinter.println("> END OF MESSAGE <");   // Notate the end of 1 message entry with "---"

        myPrinter.close();
    }

    // Create overload function for write to file to write a set of notifications.
    public void writeToFile(TreeSet<Msg> msgSet) throws IOException {
        TimeAndDate dateFormatter = new TimeAndDate();
        FileWriter write = new FileWriter (path, isAppend);
        PrintWriter myPrinter = new PrintWriter(write);
        int numMsg = msgSet.size();
        Msg msg;
        for(int i = 0; i< numMsg; i++) {
            msg = msgSet.pollFirst();
            myPrinter.println("> START OF MESSAGE <");
            myPrinter.println("Sender ID:" + msg.getSenderId());
            myPrinter.println("Read status:" + msg.isRead);
            myPrinter.println("Priority:" + msg.getPriority());
            myPrinter.println("Timestamp:" + dateFormatter.outputDATHrs(msg.getTime()));
            myPrinter.println("Message:" + msg.getMsg());
            if (msg.hasEta())
                myPrinter.println("ETA:" + msg.getEta());
            else myPrinter.println('-');
            if (msg.isLocationAvailable) {
                myPrinter.println("Location:" + msg.getLatitude() + "," + msg.getLongitude());
            } else myPrinter.println('-');
//            if(msg.hasPoliceOfficerId()) {
//                myPrinter.println("Police Officer ID:" + msg.getPoliceOfficerId());
//            }
//            else myPrinter.println('-');

            myPrinter.println("> END OF MESSAGE <");
        }

        myPrinter.close();
    }

    public static void clearInbox(String path) throws IOException {
        FileWriter write = new FileWriter (path, false);
        PrintWriter myPrinter = new PrintWriter(write);
        myPrinter = new PrintWriter(write);
        myPrinter.print("");
        myPrinter.close();
    }
}
```
###### \seedu\addressbook\parser\Parser.java
``` java
    private Command prepareRead(String args){
        try{
            final int targetIndex = parseArgsAsDisplayedIndex(args);
            return new ReadCommand(targetIndex);
        }
        catch (ParseException | NumberFormatException e){
            logr.log(Level.WARNING, "Invalid read command format.", e);
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ReadCommand.MESSAGE_USAGE));
        }
    }


```
