package seedu.addressbook.inbox;

import java.util.Date;
import java.security.Timestamp;

public class Msg {
    private String newMsg;
    private Priority priority;
    private double x, y;
    private int eta;
    private String comment;
    public boolean isRead = false;
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
