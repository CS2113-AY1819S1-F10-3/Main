//@@author ongweekeong
package seedu.addressbook.inbox;


import seedu.addressbook.common.Location;
import seedu.addressbook.password.Password;
import seedu.addressbook.timeanddate.TimeAndDate;

import java.sql.Timestamp;

/** Stores information on notifications that are sent from one user to another.
 *  @params SenderID, Read Status, Priority, timestamp, message are compulsory fields.
 * @params location (x,y coordinates) and ETA are non-compulsory fields.
 */


public class Msg implements Comparable <Msg> {
    private String senderId = Password.getID();
    private String newMsg;
    private Priority priority;
    private Location location;
    private int eta = -1;

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

    public Msg(Priority urgency, String message){
        isLocationAvailable = false;
        isRead = MESSAGE_IS_UNREAD;
        priority = urgency;
        time = new Timestamp(System.currentTimeMillis());
        newMsg = message;
    }

    public Msg(Priority urgency, String message, Location myLocale){ // Constructor for request backup message
        isLocationAvailable = true;
        isRead = MESSAGE_IS_UNREAD;
        priority = urgency;
        time = new Timestamp(System.currentTimeMillis());
        newMsg = message;
        location = myLocale;
    }

    public Msg(Priority urgency, String message, Location requesterLocale, int myEta){ // Constructor for request backup message
        isLocationAvailable = true;                                                    // An ack message should be sent to PO requesting
        isRead = MESSAGE_IS_UNREAD;                                                    // backup, stating ETA of backup deployed.
        priority = urgency;
        time = new Timestamp(System.currentTimeMillis());
        newMsg = message;
        location = requesterLocale;
        eta = myEta;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId(){
        return this.senderId;
    }

    public void setMsg(String msg){
        this.newMsg = msg;
    }

    public void setPriority(Priority urgency){
        this.priority = urgency;
    }

    public Priority getPriority(){
        return this.priority;
    }

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

    public void setMsgAsRead(){
        this.isRead = MESSAGE_IS_READ;
    }

    public double getLongitude(){
        return location.getLongitude();
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public void setEta(int eta){
        this.eta = eta;
    }

    public int getEta(){
        return this.eta;
    }

    public boolean hasEta(){
       return eta != -1;
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

    public String getTimeString(){
        return TimeAndDate.outputDATHrs(this.time);
    }

    @Override
    public int compareTo(Msg other) {
        int otherReadState = other.isRead? 1 : 0;
        int myReadState = this.isRead? 1 : 0;
        int compare = Integer.compare(myReadState, otherReadState);
        if(compare == 0) { // If same read status, compare priorities.
            compare = compareByPriority(other);
        }
        if(compare == 0){ // If priority is the same, compare by timestamp.
                compare = compareByTimestamp(other);
        }

        return compare;
    }

    public int compareByPriority(Msg other){
        return Integer.compare(other.getPriority().toInteger(), this.getPriority().toInteger());
    }

    public int compareByTimestamp(Msg other){
        return this.getTime().compareTo(other.getTime());
    }

}
