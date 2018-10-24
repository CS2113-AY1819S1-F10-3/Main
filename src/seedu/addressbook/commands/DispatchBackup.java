//@@author andyrobert3
package seedu.addressbook.commands;

import org.javatuples.Pair;
import seedu.addressbook.PatrolResourceStatus;
import seedu.addressbook.common.Location;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.Offense;
import seedu.addressbook.inbox.Msg;
import seedu.addressbook.inbox.WriteNotification;

import java.io.IOException;
import java.util.ArrayList;

public class DispatchBackup extends Command{
    public static final String COMMAND_WORD = "dispatch";

    public static final String HEADQUARTERS_NOTIFICATION_FILE = "inboxMessages/headquartersInbox";
    public static final String PO1_NOTIFICATION_FILE = "inboxMessages/PO1";
    public static final String PO2_NOTIFICATION_FILE = "inboxMessages/PO2";
    public static final String PO3_NOTIFICATION_FILE = "inboxMessages/PO3";
    public static final String PO4_NOTIFICATION_FILE = "inboxMessages/PO4";
    public static final String PO5_NOTIFICATION_FILE = "inboxMessages/PO5";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Dispatch help from headquarters.\n\t"
            + "Example: " + COMMAND_WORD
            + " PO1 gun PO3";

    public static String MESSAGE_REQUEST_SUCCESS = "Dispatch for backup is successful.";
    public static String MESSAGE_INVALID_POLICE_ID = "Police ID does not exist.";


    private WriteNotification writeNotificationToBackupOfficer;
    private WriteNotification writeNotificationToRequester;
    private String backupOfficer;
    private String requester;
    private String offense;

    private String getNotificationFilePath(String policeId) {
        switch (policeId) {
            case "hqp":
                return HEADQUARTERS_NOTIFICATION_FILE;
            case "po1":
                return PO1_NOTIFICATION_FILE;
            case "po2":
                return PO2_NOTIFICATION_FILE;
            case "po3":
                return PO3_NOTIFICATION_FILE;
            case "po4":
                return PO4_NOTIFICATION_FILE;
            case "po5":
                return PO5_NOTIFICATION_FILE;
        }

        return MESSAGE_INVALID_POLICE_ID;
    }

    public DispatchBackup(String backupOfficer, String requester, String caseName) {
        writeNotificationToBackupOfficer = new WriteNotification(getNotificationFilePath(backupOfficer), true);
        writeNotificationToRequester = new WriteNotification(getNotificationFilePath(requester), true);

        this.offense = caseName;
        this.backupOfficer = backupOfficer;
        this.requester = requester;
    }

    public CommandResult execute()  {
        Location origin = PatrolResourceStatus.getLocation(backupOfficer);

        ArrayList<Location> destinationList = new ArrayList<>();
        destinationList.add(PatrolResourceStatus.getLocation(requester));
        ArrayList<Pair<Integer, String>> etaList = origin.getEtaFrom(destinationList);

        int eta = etaList.get(0).getValue0();
        String etaMessage = etaList.get(0).getValue1();

        try {
            Msg dispatchMessage = new Msg(Offense.getPriority(offense), etaMessage, PatrolResourceStatus.getLocation(requester), eta, requester);
            dispatchMessage.setPoliceOfficerId(requester);
            writeNotificationToBackupOfficer.writeToFile(dispatchMessage);


            // TODO: Backup is not available
            Msg requesterMessage = new Msg(Offense.getPriority(offense), etaMessage, PatrolResourceStatus.getLocation(backupOfficer), eta, backupOfficer);
            writeNotificationToRequester.writeToFile(requesterMessage);
        } catch(IOException ioe) {
            return new CommandResult(Messages.MESSAGE_SAVE_ERROR);
        } catch (IllegalValueException ioe) {
            return new CommandResult(Messages.MESSAGE_ERROR); // TODO: Find proper message
        }

        return new CommandResult(MESSAGE_REQUEST_SUCCESS);
    }
}
