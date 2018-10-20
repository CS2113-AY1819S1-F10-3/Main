package seedu.addressbook.timeanddate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeAndDate {
    private static Timestamp currentDAT = new Timestamp(System.currentTimeMillis());
    private SimpleDateFormat timeStampFormatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
    private SimpleDateFormat screeningTimestampFormatter = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
    private String outputDAT = timeStampFormatter.format(currentDAT);
    private String outputScreeningDAT = screeningTimestampFormatter.format(currentDAT);
    public String outputDATHrs(){
        return outputDAT + "hrs";
    }
    public String getOutputScreeningDAT(){
        return outputScreeningDAT;
    }
}
