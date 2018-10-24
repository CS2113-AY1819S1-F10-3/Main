//@@author ongweekeong
package seedu.addressbook.inbox;

import seedu.addressbook.common.Location;

import java.sql.Timestamp;

/** Msg has the following attributes:
 *  @params Priority, timestamp, message, location (x,y coordinates) and ETA.
 *  Priority, timestamp and message are compulsory fields. Location and ETA are optional.
 */


public class Msg implements Comparable <Msg> {
//    protected String senderId;
//    protected String receiverId;
    private String newMsg;
    private Priority priority;
    private Location location;
    private int eta = -1;

    private String policeOfficerId;

    //private String comment;
    protected boolean isRead;
    protected boolean isLocationAvailable;
    private Timestamp time;
    public static final boolean MESSAGE_IS_READ = true;
    public static final boolean MESSAGE_IS_UNREAD = false;

    public enum Priority {
        HIGH(3),   // For messages that require HPQ intervention
        MED(2),    // For messages that only require PO back-up
        LOW(1);     // Messages that are FYI (e.g. Notifications to admin that details of subjects have changed

        private int priority;

        Priority (int priority) {
            this.priority = priority;
        }

        public int toInteger() {
            return priority;
        }

    }

    public Msg(){
        isLocationAvailable = false;
        isRead = MESSAGE_IS_UNREAD;
    }

    public Msg(Priority urgency, String message, String policeOfficerId){
        isLocationAvailable = false;
        isRead = MESSAGE_IS_UNREAD;
        priority = urgency;
        time = new Timestamp(System.currentTimeMillis());
        newMsg = message;
        this.policeOfficerId = policeOfficerId;
    }

    public Msg(Priority urgency, String message, Location myLocale){ // Constructor for request backup message
        isLocationAvailable = true;
        isRead = MESSAGE_IS_UNREAD;
        priority = urgency;
        time = new Timestamp(System.currentTimeMillis());
        newMsg = message;
        location = myLocale;
    }

    public Msg(Priority urgency, String message, Location requesterLocale, int myEta, String policeOfficerId){ // Constructor for request backup message
        isLocationAvailable = true;                                                    // An ack message should be sent to PO requesting
        isRead = MESSAGE_IS_UNREAD;                                                    // backup, stating ETA of backup deployed.
        priority = urgency;
        time = new Timestamp(System.currentTimeMillis());
        newMsg = message;
        location = requesterLocale;
        eta = myEta;
        this.policeOfficerId = policeOfficerId;
    }




    public void addMsg(String msg){
        this.newMsg = msg;
    }

    public void setPriority(Priority urgency){
        this.priority = urgency;
    }

    public Priority getPriority(){
        return this.priority;
    }

    public void setMsg(String message) { this.newMsg = message; }
    public String getMsg(){
        return this.newMsg;
    }

    public void setLocation(Location place){
        this.location = place;
        isLocationAvailable = true;
    }

    public Location getLocation(){
        return location;
    }

    public double getLongitude(){
        return location.getLongitude();
    }
    public void setLongitude(double x){
        location.setLongitude(x);
    }

    public double getLatitude(){
        return location.getLatitude();
    }
    public void setLatitude(double y){
        location.setLatitude(y);
    }

    public int getEta(){
        return this.eta;
    }
    public void setEta(int eta){
        this.eta = eta;
    }

    public boolean hasEta(){
       return eta != -1;
    }

    public String getPoliceOfficerId() {
        return policeOfficerId;
    }
    public void setPoliceOfficerId(String policeOfficerId) { this.policeOfficerId = policeOfficerId; }

    public boolean hasPoliceOfficerId() {
        return policeOfficerId != null;
    }

    public void setTime(){
        time = new Timestamp(System.currentTimeMillis()); // Set to current time if no timestamp passed.
    }

    public void setTime(Timestamp time){
        this.time = time;
    }

    public Timestamp getTime(){
        return this.time;
    }

    @Override
    public int compareTo(Msg other) {
        int otherInt = other.isRead? 1 : 0;
        int myInt = this.isRead? 1 : 0;
        int compare = Integer.compare(myInt, otherInt);
        if(compare == 0){ // If same read status, compare priorities.
            compare = compareByPriority(other);
            if(compare == 0){ // If priority is the same, compare by timestamp.
                compare = compareByTimestamp(other);
            }
            return compare;
        }
        else{
            return compare;
        }
    }

    // TODO:
    public int compareByPriority(Msg other){
        return Integer.compare(other.getPriority().toInteger(), this.getPriority().toInteger());
    }

    public int compareByTimestamp(Msg other){
        return this.getTime().compareTo(other.getTime());
    }

}
