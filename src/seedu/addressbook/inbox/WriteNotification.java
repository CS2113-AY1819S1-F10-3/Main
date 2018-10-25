//@@author ongweekeong
package seedu.addressbook.inbox;

import seedu.addressbook.timeanddate.TimeAndDate;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeSet;

public class WriteNotification {
    private String path;
    private boolean isAppend = false;
    protected MessageFilePaths msgFilePaths = new MessageFilePaths();

    public WriteNotification(String userId){
        path = msgFilePaths.getFilePathFromUserId(userId);
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

//        if(message.hasPoliceOfficerId()) {
//            myPrinter.println("Police Officer ID:" + message.getPoliceOfficerId());
//        }
//        else myPrinter.println('-');
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
            myPrinter.println("Timestamp:" + dateFormatter.outputDATHrs());
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

}
