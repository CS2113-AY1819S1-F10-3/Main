package seedu.addressbook.data.PoliceOfficers;

import seedu.addressbook.Location;

import seedu.addressbook.Location;

import java.sql.Timestamp;



public class Case {

    public static final String EXAMPLE_CASE = "PO3, robbery, GPS coordinates, timestamp";
    public static final String MESSAGE_CASE_CONSTRAINTS = "Case must have: PatrolID, message, GPS, timestamp";

    public static PatrolID attendingPO;
    public String caseMessage;
    public static Location gpsCoordinates;
    public static Timestamp caseTimeStamp;


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
        this.caseTimeStamp = dateTime;
        this.value = patrolIDNo.patrolID + SPACE + caseMessage + SPACE + Double.toString(location.getLongitude()) + SPACE +
                Double.toString(location.getLatitude()) + SPACE + dateTime;

    }

    public String PrintCase() {return value;}

    public String getAttendingPO(){return attendingPO.patrolID;}

    public String getCaseMessage() {return caseMessage;}

    public String getGpsCoorinates() {return Double.toString(gpsCoordinates.getLongitude()) + SPACE + Double.toString(gpsCoordinates.getLatitude());}

    public Timestamp getCaseTimeStamp() {return caseTimeStamp;}

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
