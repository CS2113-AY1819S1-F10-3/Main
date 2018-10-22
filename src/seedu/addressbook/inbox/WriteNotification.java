package seedu.addressbook.inbox;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeSet;

public class WriteNotification {
    private String path;
    private boolean isAppend = false;

    public WriteNotification(String filePath){
        path = filePath;
    }

    public WriteNotification(String filePath, boolean appendValue){
        path = filePath;
        isAppend = appendValue;
    }


    /**	Message format should look like this
     *	Read/Unread (1 or 0) --> for writeToFile function, messages are entered as unread.
     *	Priority of Message
     *	Timestamp of message
     *	Message
     *  ETA, if applicable
     *  Location, if available
     */

    public void writeToFile(Msg message) throws IOException{

        FileWriter write = new FileWriter (path, isAppend);
        PrintWriter myPrinter = new PrintWriter(write);
        myPrinter.println("> START OF MESSAGE <");
        myPrinter.println("Read status:" + message.isRead);
        myPrinter.println("Priority:" + message.getPriority());
        myPrinter.println("Timestamp:" + message.getTime());
        myPrinter.println("Message:" + message.getMsg());

        if(message.hasEta())
            myPrinter.println("ETA:" + message.getEta());
        else myPrinter.println('-');

        if(message.isLocationAvailable) {
            myPrinter.println("Location:" + message.getLatitude() + "," + message.getLongitude());
        }
        else myPrinter.println('-');

        if(message.hasPoliceOfficerId()) {
            myPrinter.println("Police Officer ID:" + message.getPoliceOfficerId());
        }
        else myPrinter.println('-');
        myPrinter.println("> END OF MESSAGE <");   // Notate the end of 1 message entry with "---"
        myPrinter.close();
    }

    // Create overload function for write to file to write a set of notifications.
    public void writeToFile(TreeSet<Msg> msgSet) throws IOException {

        FileWriter write = new FileWriter (path, isAppend);
        PrintWriter myPrinter = new PrintWriter(write);
        int numMsg = msgSet.size();
        Msg msg;
        for(int i = 0; i< numMsg; i++) {
            msg = msgSet.pollLast(); // TODO: Understand if saving first to last or last to first is more efficient.
            myPrinter.println("> START OF MESSAGE <");
            myPrinter.println("Read status:" + msg.isRead);
            myPrinter.println("Priority:" + msg.getPriority());
            myPrinter.println("Timestamp:" + msg.getTime());
            myPrinter.println("Message:" + msg.getMsg());
            if (msg.hasEta())
                myPrinter.println("ETA:" + msg.getEta());
            else myPrinter.println('-');
            if (msg.isLocationAvailable) {
                myPrinter.println("Location:" + msg.getLatitude() + "," + msg.getLongitude());
            } else myPrinter.println('-');
            if(msg.hasPoliceOfficerId()) {
                myPrinter.println("Police Officer ID:" + msg.getPoliceOfficerId());
            }
            else myPrinter.println('-');

            myPrinter.println("> END OF MESSAGE <");
        }

        myPrinter.close();
    }

}
