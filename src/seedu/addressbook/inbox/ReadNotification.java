package seedu.addressbook.inbox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadNotification {
    private String path;
    private Msg returnMsg;

    public ReadNotification(String filePath) {
        path = filePath;

    }

    public Msg ReadFromFile() throws IOException {  // If no new notifications and 'inbox' command invoked, show past 10 notifications
        String line;
        BufferedReader br = new BufferedReader(new FileReader(path));
        while ((line = br.readLine()) != null) {
            if (line == "> END OF MESSAGE <")   // End of message entry
                return returnMsg;
            else {
                String[] parts = line.split(":", 2);
                String msgType = parts[0];
                if (parts.length == 2) {
                    if (msgType == "Read status") {
                        returnMsg.isRead = Boolean.parseBoolean(parts[1]);
                    } else if (msgType == "Priority") {
                        Msg.Priority msgPriority;
                        if (parts[1] == "HIGH")
                            msgPriority = Msg.Priority.HIGH;
                        else if (parts[1] == "MED")
                            msgPriority = Msg.Priority.MED;
                        else
                            msgPriority = Msg.Priority.LOW;
                        returnMsg.setPriority(msgPriority);
                    } else if (msgType == "Timestamp") {
                        SimpleDateFormat timeFormatted = new SimpleDateFormat("dd.MM.YYYY.HH.MM.ss");
                        Date parsedTimeStamp;
                        try {
                            parsedTimeStamp = timeFormatted.parse(line);
                        } catch (ParseException e) {
                            continue;   // Find a way to debug this if time format is invalid.
                        }
                        Timestamp msgTime = new Timestamp(parsedTimeStamp.getTime());
                        returnMsg.setTime(msgTime);
                    } else if (msgType == "Message") {
                        returnMsg.addMsg(parts[1]); // Multiple lines of message required or not?
                    } else if (msgType == "ETA") {
                        returnMsg.setEta(Integer.parseInt(parts[1]));
                    } else if (msgType == "Location") {
                        String[] coordinates = parts[1].split(",", 2);
                        returnMsg.setLocation(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
                    }
                }
            }
        }
        return returnMsg;
    }
}
