package seedu.addressbook.inbox;

import java.util.Date;

public class Msg {
    public static String newMsg;
    public static Priority priority;
    public static double x, y;
    public static int eta;
    public static boolean isRead = false;
    public enum Priority {
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
}
