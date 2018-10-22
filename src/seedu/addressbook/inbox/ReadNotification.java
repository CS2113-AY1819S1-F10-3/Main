//@@author ongweekeong
package seedu.addressbook.inbox;

import seedu.addressbook.Location;

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
    private static Msg returnMsg;
    private int unreadMsgs = 0;
    protected static TreeSet<Msg> sortedMsgs = new TreeSet<>();

    public ReadNotification(String filePath) {
        path = filePath;
    }

    /*public static HashMap<Triplet<Boolean, Msg.Priority, Timestamp>, Triplet<String, Integer, Location>>
    ReadFromFile() throws IOException {  // If no new notifications and 'inbox' command invoked, show past 10 notifications
        String line;
        BufferedReader br = new BufferedReader(new FileReader(path));
        while ((line = br.readLine()) != null) {
            if (line.equals("> START OF MESSAGE <")) {
                returnMsg = new Msg();
            }
            else if (line.equals("> END OF MESSAGE <"))   // End of message entry, store into hashmap
                allMsg.put(new Triplet<>(returnMsg.isRead, returnMsg.getPriority(), returnMsg.getTime()),
                        new Triplet<>(returnMsg.getMsg(), returnMsg.getEta(), returnMsg.getLocation()));
            else {
                String[] parts = line.split(":", 2);
                String msgType = parts[0];
                if (parts.length == 2) {
                    if (msgType.equals("Read status")) {
                        returnMsg.isRead = Boolean.parseBoolean(parts[1]);
                        if (!returnMsg.isRead)
                            numUnreadMsgs += 1;
                    }
                    else if (msgType.equals("Priority")) {
                        Msg.Priority msgPriority;
                        if (parts[1].equals("HIGH"))
                            msgPriority = Msg.Priority.HIGH;
                        else if (parts[1].equals("MED"))
                            msgPriority = Msg.Priority.MED;
                        else
                            msgPriority = Msg.Priority.LOW;
                        returnMsg.setPriority(msgPriority);
                    }
                    else if (msgType.equals("Timestamp")) {
                        //SimpleDateFormat timeFormatted = new SimpleDateFormat("dd.MM.YYYY.HH.MM.ss");
                        SimpleDateFormat timeFormatted = new SimpleDateFormat("YYYY-MM-dd HH:MM:ss.SSS");
                        Date parsedTimeStamp;
                        try {
                            parsedTimeStamp = timeFormatted.parse(parts[1]);
                        } catch (ParseException e) {
                            continue;   // Find a way to debug this if time format is invalid.
                        }
                        Timestamp msgTime = new Timestamp(parsedTimeStamp.getTime());
                        returnMsg.setTime(msgTime);
                    }
                    else if (msgType.equals("Message")) {
                        returnMsg.addMsg(parts[1]); // Multiple lines of message required or not?
                    }
                    else if (msgType.equals("ETA")) {
                        returnMsg.setEta(Integer.parseInt(parts[1]));
                    }
                    else if (msgType.equals("Location")) {
                        String[] coordinates = parts[1].split(",", 2);
                        Location myLocation = new Location(Double.parseDouble(coordinates[0]),
                                Double.parseDouble(coordinates[1]));
                        returnMsg.setLocation(myLocation);
                    }
                }

            }
        }
        return allMsg;
    }*/

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
                    if (msgType.equals("Read status")) {
                        returnMsg.isRead = Boolean.parseBoolean(parts[1]);
                        if (!returnMsg.isRead)
                            unreadMsgs += 1;
                    }
                    else if (msgType.equals("Priority")) {
                        Msg.Priority msgPriority;
                        if (parts[1].equals("HIGH"))
                            msgPriority = Msg.Priority.HIGH;
                        else if (parts[1].equals("MED"))
                            msgPriority = Msg.Priority.MED;
                        else
                            msgPriority = Msg.Priority.LOW;
                        returnMsg.setPriority(msgPriority);
                    }
                    else if (msgType.equals("Timestamp")) {
                        SimpleDateFormat timeFormatted = new SimpleDateFormat("dd/MM/yyyy HHmm:ss");
                        Date parsedTimeStamp;
                        try {
                            parsedTimeStamp = timeFormatted.parse(parts[1]);
                        } catch (ParseException e) {
                            continue;   // Find a way to debug this if time format is invalid.
                        }
                        Timestamp msgTime = new Timestamp(parsedTimeStamp.getTime());
                        returnMsg.setTime(msgTime);
                    }
                    else if (msgType.equals("Message")) {
                        returnMsg.addMsg(parts[1]);
                    }
                    else if (msgType.equals("ETA")) {
                        returnMsg.setEta(Integer.parseInt(parts[1]));
                    }
                    else if (msgType.equals("Location")) {
                        String[] coordinates = parts[1].split(",", 2);
                        Location myLocation = new Location(Double.parseDouble(coordinates[0]),
                                Double.parseDouble(coordinates[1]));
                        returnMsg.setLocation(myLocation);
                    }
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
    /*public static void main(String[] args) throws IOException {
        ReadNotification myFile = new ReadNotification("notifications.txt");
        int messageNum = 1;
        sortedMsgs = ReadFromFile();
        System.out.println(unreadMsgs);
        System.out.println(sortedMsgs.size());
        while(!sortedMsgs.isEmpty()) {
            Msg msgToPrint = sortedMsgs.pollFirst();
            try {

                System.out.println(messageNum + " Priority: " + msgToPrint.getPriority() + ", Sent: " + msgToPrint.getTime() +
                        ", message: " + msgToPrint.getMsg() + ", Coordinates: " + msgToPrint.getLatitude() + ", " +
                        msgToPrint.getLongitude() + ", ETA: " + msgToPrint.getEta() + '.');
            }
            catch(Exception e){
                System.out.println(messageNum + " Priority: " + msgToPrint.getPriority() + ", Sent: " + msgToPrint.getTime() +
                        ", message: " + msgToPrint.getMsg() + '.');
            }
            messageNum++;
        }

    }*/
}
