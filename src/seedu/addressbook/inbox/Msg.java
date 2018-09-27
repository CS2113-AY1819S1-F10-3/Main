package seedu.addressbook.inbox;

import java.security.Timestamp;

/** Msg has the following attributes:
 *  @params Priority, timestamp, message, location (x,y coordinates) and ETA.
 *  Priority, timestamp and message are compulsory fields. Location and ETA are optional.
 */


public class Msg {
    private String newMsg;
    private Priority priority;
    private double x, y;
    private int eta = -1;
    private String comment;
    public boolean isRead = false;
    public boolean isLocationAvailable = false;
    private Timestamp time;
    private enum Priority {
        HIGH,   // For messages that require HPQ intervention
        MED,    // For messages that only require PO back-up
        LOW     // Messages that are FYI (e.g. Notifications to admin that details of subjects have changed
    }

    public Msg(){
        Msg message = new Msg();
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
    public String getMsg(){
        return this.newMsg;
    }

    public void setLocation(double x, double y, double min){
        this.x = x;
        this.y = y;
        isLocationAvailable = true;
    }

    public double getLongitude(){
        return this.x;
    }

    public double getLatitude(){
        return this.y;
    }
    public void setEta(int eta){
        this.eta = eta;
    }
    public int getEta(){
        return this.eta;
    }

    public boolean hasEta(){
        if(eta == -1)
            return false;
        else
            return true;
    }

    public void setTime(Timestamp time){
        this.time = time;
    }

    public Timestamp getTime(){
        return this.time;
    }

    public void setComments(String comment){
        this.comment = comment;
    }
}
