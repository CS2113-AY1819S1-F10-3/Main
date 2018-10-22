package seedu.addressbook.timeanddate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

//@@author iamputradanish
public class TimeAndDate {
    private static Timestamp currentDAT = new Timestamp(System.currentTimeMillis());
    private SimpleDateFormat timeStampFormatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
    private String outputDAT = timeStampFormatter.format(currentDAT);
    public String outputDATHrs(){
        return outputDAT + "hrs";
    }
}
