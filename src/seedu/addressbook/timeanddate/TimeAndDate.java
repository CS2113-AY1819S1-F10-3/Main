package seedu.addressbook.timeanddate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeAndDate {
    private static Timestamp currentDAT = new Timestamp(System.currentTimeMillis());
    private SimpleDateFormat timeStampFormatter = new SimpleDateFormat("dd/MM/yyyy HHmm:ss");
    private String outputDAT = timeStampFormatter.format(currentDAT);
    public String outputDATHrs(){
        return outputDAT + "hrs";
    }
    public String outputDATHrs(Timestamp theTime){
        String outputDAT = timeStampFormatter.format(theTime);
        return outputDAT + "hrs";
    }
}
