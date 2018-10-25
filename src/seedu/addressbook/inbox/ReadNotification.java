//@@author ongweekeong
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
        SimpleDateFormat timeFormatted = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
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