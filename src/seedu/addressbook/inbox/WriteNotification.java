package seedu.addressbook.inbox;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
        myPrinter.println(message.isRead);
        myPrinter.println(message.getPriority());
        myPrinter.println(message.getTime());
        myPrinter.println(message.getMsg());
        if(message.hasEta())
            myPrinter.println(message.getEta());
        if(message.isLocationAvailable) {
            myPrinter.println(message.getLatitude());
            myPrinter.println(message.getLongitude());
        }
        myPrinter.println("---");   // Notate the end of 1 message entry with "---"
        myPrinter.close();
    }

}
