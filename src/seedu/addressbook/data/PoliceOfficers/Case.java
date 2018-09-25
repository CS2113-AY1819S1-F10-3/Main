package seedu.addressbook.data.PoliceOfficers;

import seedu.addressbook.data.exception.IllegalValueException;

import java.sql.Timestamp;
import java.time.Instant;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Case {

    public static final String EXAMPLE_CASE = "PO3, robbery, GPS coordinates, timestamp";
    public static final String MESSAGE_CASE_CONSTRAINTS = "Case must have: PatrolID, message, GPS, timestamp";

    public static final PatrolID attendingPO;
    public String caseMessage;
    //public GPS coordinates;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static final String caseDateTime;

    private String SPACE = ", ";
    public String value;

    public Case(){
        this.attendingPO = null;
        this.caseMessage  = "none";
        //this.GPS = null
        this.caseDateTime = null;
    }
    public Case(PatrolID patrolIDNo, String message, /*GPS*/ Date dateTime) throws IllegalValueException{
        if (!isValidCase){
            throw new IllegalValueException(MESSAGE_CASE_CONSTRAINTS);
        }
        this.attendingPO = patrolIDNo;
        this.caseMessage = message;
        //this.GPSCoordinates = GPS;
        this.caseDateTime = dateFormat.format(dateTime);
        this.value = patrolIDNo.patrolID + SPACE + caseMessage + SPACE + /*GPS*/ caseDateTime;

    }

    public static boolean isValidCase() //to be confirmed

}
