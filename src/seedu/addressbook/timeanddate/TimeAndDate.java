package seedu.addressbook.timeanddate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

//@@author iamputradanish
public class TimeAndDate {
    private Timestamp currentDAT = new Timestamp(System.currentTimeMillis());
    private SimpleDateFormat timeStampFormatter = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss:SSS");
    private String outputDAT = timeStampFormatter.format(currentDAT);

    public String outputDATHrs(){
        return outputDAT + "hrs";
    }

    public String outputDATHrs(Timestamp theTime){
        String outputDAT = timeStampFormatter.format(theTime);
        return outputDAT + "hrs";

    }
}
