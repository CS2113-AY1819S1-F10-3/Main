//@@author andyrobert3
package seedu.addressbook.commands;

import org.javatuples.Pair;
import org.json.JSONException;
import seedu.addressbook.PatrolResourceStatus;
import seedu.addressbook.common.Location;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.exception.PatrolResourceUnavailableException;
import seedu.addressbook.data.person.Offense;
import seedu.addressbook.inbox.Msg;
import seedu.addressbook.inbox.WriteNotification;

import java.io.IOException;
import java.util.ArrayList;

public class DispatchCommand extends Command{
    public static final String COMMAND_WORD = "dispatch";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Dispatch help from headquarters.\n\t"
            + "Example: " + COMMAND_WORD
            + " PO1 gun PO3";

    public static String MESSAGE_DISPATCH_SUCCESS = "Dispatch for %s backup is successful.\n";

    public static String MESSAGE_OFFICER_UNAVAILABLE = "Please choose another officer to send for backup.\n\t"
            + "Use 'checkstatus' command to see engaged/free officers.";

    private WriteNotification writeNotificationToBackupOfficer;
    private WriteNotification writeNotificationToRequester;
    private Location origin;
    private ArrayList<Location> destinationList;
    private String backupOfficer;
    private String requester;
    private String offense;

    public DispatchCommand(String backupOfficer, String requester, String caseName) {
        writeNotificationToBackupOfficer = new WriteNotification(backupOfficer);
        writeNotificationToRequester = new WriteNotification(requester);

        this.offense = caseName;
        this.backupOfficer = backupOfficer;
        this.requester = requester;
        this.origin = PatrolResourceStatus.getLocation(backupOfficer);
        this.destinationList = new ArrayList<>();
    }

    public CommandResult execute()  {
        try {
            destinationList.add(PatrolResourceStatus.getLocation(requester));
            ArrayList<Pair<Integer, String>> etaList = origin.getEtaFrom(destinationList);

            int eta = etaList.get(0).getValue0();
            String etaMessage = etaList.get(0).getValue1();

            String dispatchStringMessage = "ETA " + etaMessage + ", Location of Requester: " +
                    PatrolResourceStatus.getLocation(backupOfficer).getGoogleMapsURL();

            String requesterStringMessage = "ETA " + etaMessage + ", Location of Backup: " +
                    PatrolResourceStatus.getLocation(backupOfficer).getGoogleMapsURL();

            Msg dispatchMessage = new Msg(Offense.getPriority(offense), dispatchStringMessage,
                                            PatrolResourceStatus.getLocation(requester), eta);

            if (PatrolResourceStatus.getPatrolResource(backupOfficer).getValue2()) {
                throw new PatrolResourceUnavailableException(backupOfficer);
            }
            
            Msg requesterMessage = new Msg(Offense.getPriority(offense), requesterStringMessage,
                                            PatrolResourceStatus.getLocation(backupOfficer), eta);

            writeNotificationToRequester.writeToFile(requesterMessage);
            writeNotificationToBackupOfficer.writeToFile(dispatchMessage);
        } catch (IOException ioe) {
            return new CommandResult(Messages.MESSAGE_SAVE_ERROR);
        } catch (IllegalValueException ioe) {
            return new CommandResult(Offense.MESSAGE_OFFENSE_INVALID + "\n" +
                    Offense.getListOfValidOffences());
        } catch (JSONException jse) {
            return new CommandResult(Messages.MESSAGE_JSON_PARSE_ERROR);
        } catch (PatrolResourceUnavailableException prue) {
            return new CommandResult(prue.getMessage() + "\n" + MESSAGE_OFFICER_UNAVAILABLE);
        }

        return new CommandResult(String.format(MESSAGE_DISPATCH_SUCCESS, requester));
    }
}
