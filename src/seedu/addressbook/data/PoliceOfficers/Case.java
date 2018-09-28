package seedu.addressbook.data.PoliceOfficers;

import seedu.addressbook.Location;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class Case {

    public static final String EXAMPLE_CASE = "PO3, robbery, GPS coordinates, timestamp";
    public static final String MESSAGE_CASE_CONSTRAINTS = "Case must have: PatrolID, message, GPS, timestamp";

    public static PatrolID attendingPO;
    public String caseMessage;
    public static Location gpsCoordinates;
    public static Timestamp caseTimeStamp;
    public static String caseTimeStampFormatted;
    private static final SimpleDateFormat timestampFormatter = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss");


    private String SPACE = ", ";
    public String value;

    public Case(){
        this.attendingPO = null;
        this.caseMessage  = "none";
        this.gpsCoordinates = null;
        this.caseTimeStamp = null;
    }
    public Case(PatrolID patrolIDNo, String message, Location location, Timestamp dateTime){

        this.attendingPO = patrolIDNo;
        this.caseMessage = message;
        this.gpsCoordinates = location;
        this.caseTimeStampFormatted = timestampFormatter.format(dateTime);
        this.value = patrolIDNo.patrolID + SPACE + caseMessage + SPACE + Double.toString(location.getLongitude()) + SPACE +
                Double.toString(location.getLatitude()) + SPACE + timestampFormatter.format(dateTime);

    }

    public String PrintCase() {return value;}

    public String getAttendingPO(){return attendingPO.patrolID;}

    public String getCaseMessage() {return caseMessage;}

    public String getGpsCoorinates() {return Double.toString(gpsCoordinates.getLongitude()) + SPACE + Double.toString(gpsCoordinates.getLatitude());}

    public Timestamp getCaseTimeStamp() {return caseTimeStamp;}

    public String getCaseTimeStampFormatted() {return caseTimeStampFormatted;}

    /*public static void main(String[] args) {
        Location location = new Location(-6.206968,106.751365);
        Location origin = new Location(-6.189482, 106.733902);
        PatrolID id = new PatrolID(5);
        String msg = "Fire";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Case c = new Case(id,msg,location,now);
        System.out.print(c.PrintCase());


    }*/

}
