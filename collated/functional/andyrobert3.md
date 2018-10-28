# andyrobert3
###### \seedu\addressbook\commands\Command.java
``` java
    protected ReadOnlyPerson getTargetPerson(NRIC nric) throws UniquePersonList.PersonNotFoundException {
        for (ReadOnlyPerson person: relevantPersons) {
            if (person.getNric().getIdentificationNumber().equals(nric.getIdentificationNumber())) {
                return person;
            }
        }
        throw new UniquePersonList.PersonNotFoundException();
    }


    public int getTargetIndex() {
        return targetIndex;
    }

    public void setTargetIndex(int targetIndex) {
        this.targetIndex = targetIndex;
    }
}
```
###### \seedu\addressbook\commands\DispatchBackup.java
``` java
package seedu.addressbook.commands;

import org.javatuples.Pair;
import seedu.addressbook.PatrolResourceStatus;
import seedu.addressbook.common.Location;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.Offense;
import seedu.addressbook.inbox.MessageFilePaths;
import seedu.addressbook.inbox.Msg;
import seedu.addressbook.inbox.WriteNotification;

import java.io.IOException;
import java.util.ArrayList;

public class DispatchBackup extends Command{
    public static final String COMMAND_WORD = "dispatch";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Dispatch help from headquarters.\n\t"
            + "Example: " + COMMAND_WORD
            + " PO1 gun PO3";

    public static String MESSAGE_REQUEST_SUCCESS = "Dispatch for backup is successful.";

    private WriteNotification writeNotificationToBackupOfficer;
    private WriteNotification writeNotificationToRequester;
    private String backupOfficer;
    private String requester;
    private String offense;

    public DispatchBackup(String backupOfficer, String requester, String caseName) {
        writeNotificationToBackupOfficer = new WriteNotification(MessageFilePaths.getFilePathFromUserId(backupOfficer), true);
        writeNotificationToRequester = new WriteNotification(MessageFilePaths.getFilePathFromUserId(requester), true);

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
            Msg dispatchMessage = new Msg(Offense.getPriority(offense), "ETA " + etaMessage,
                                            PatrolResourceStatus.getLocation(requester), eta);
            //dispatchMessage.setPoliceOfficerId(requester);
            writeNotificationToBackupOfficer.writeToFile(dispatchMessage);


            // TODO: Backup is not available
            Msg requesterMessage = new Msg(Offense.getPriority(offense), "ETA " + etaMessage,
                                            PatrolResourceStatus.getLocation(backupOfficer), eta);
            writeNotificationToRequester.writeToFile(requesterMessage);
        } catch(IOException ioe) {
            return new CommandResult(Messages.MESSAGE_SAVE_ERROR);
        } catch (IllegalValueException ioe) {
            return new CommandResult(Messages.MESSAGE_ERROR); // TODO: Find proper message
        }

        return new CommandResult(MESSAGE_REQUEST_SUCCESS);
    }
}
```
###### \seedu\addressbook\commands\EditCommand.java
``` java
package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;

import java.util.Set;

/**
 * Edits existing person in police records.
 */
public class EditCommand extends Command {
    private String nric;
    private String postalCode;
    private String status;
    private String wantedFor;
    private Set<String> offenses;

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Edits the person identified by the NRIC number.\n\t"
            + "Contact details can be marked private by prepending 'p' to the prefix.\n\t"
            + "Parameters: n/NRIC p/POSTALCODE s/STATUS w/WANTEDFOR [o/PASTOFFENSES]...\n\t"
            + "Example: " + COMMAND_WORD
            + " n/s1234567a p/510247 s/wanted w/murder o/gun";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %s";

    private void updatePerson() throws IllegalValueException, UniquePersonList.PersonNotFoundException {
        for (Person person : addressBook.getAllPersons()) {
            if (person.getNric().getIdentificationNumber().equals(this.nric)) {
                person.setPostalCode(new PostalCode(postalCode));
                person.setWantedFor(new Offense(wantedFor));
                person.setStatus(new Status(status));
                person.addPastOffenses(Offense.getOffenseSet(offenses));

                return;
            }
        }

        throw new UniquePersonList.PersonNotFoundException();
    }

    public EditCommand(String nric,
                       String postalCode,
                       String status,
                       String wantedFor,
                       Set<String> offenses) {

        this.nric = nric;
        this.postalCode = postalCode;
        this.status = status;
        this.wantedFor = wantedFor;
        this.offenses = offenses;
    }

    @Override
    public CommandResult execute() throws IllegalValueException {
        try {
            this.updatePerson();
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, this.nric));
        } catch(UniquePersonList.PersonNotFoundException pnfe) {
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        }

    }
}
```
###### \seedu\addressbook\commands\RequestHelp.java
``` java
package seedu.addressbook.commands;

import seedu.addressbook.PatrolResourceStatus;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.Offense;
import seedu.addressbook.inbox.MessageFilePaths;
import seedu.addressbook.inbox.Msg;
import seedu.addressbook.inbox.WriteNotification;
import seedu.addressbook.password.Password;

import java.io.IOException;

public class RequestHelp extends Command {
    public static final String COMMAND_WORD = "request";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Requests help from headquarters.\n\t"
            + "Message from police officer can be appended in text.\n\t"
            + "Example: " + COMMAND_WORD
            + " gun "
            + " Help needed on Jane Street";


    public static String MESSAGE_REQUEST_SUCCESS = "Request for backup is successful.";

    private Msg requestHelpMessage;
    private WriteNotification writeNotification;

    public RequestHelp(String caseName, String messageString) throws IllegalValueException {
        writeNotification = new WriteNotification(MessageFilePaths.FILEPATH_HQP_INBOX, true);
        requestHelpMessage = new Msg(Offense.getPriority(caseName), messageString, PatrolResourceStatus.getLocation(Password.getID()));
    }


    @Override
    public CommandResult execute() {
        try {
            writeNotification.writeToFile(requestHelpMessage);
            return new CommandResult(String.format(MESSAGE_REQUEST_SUCCESS));
        } catch (IOException ioe) {
            return new CommandResult(Messages.MESSAGE_SAVE_ERROR);
        }
    }


}
```
###### \seedu\addressbook\commands\RequestHelpCommand.java
``` java
package seedu.addressbook.commands;

import seedu.addressbook.PatrolResourceStatus;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.Offense;
import seedu.addressbook.inbox.MessageFilePaths;
import seedu.addressbook.inbox.Msg;
import seedu.addressbook.inbox.WriteNotification;
import seedu.addressbook.password.Password;

import java.io.IOException;

public class RequestHelpCommand extends Command {
    public static final String COMMAND_WORD = "request";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Requests help from headquarters.\n\t"
            + "Message from police officer can be appended in text.\n\t"
            + "Example: " + COMMAND_WORD
            + " gun "
            + " Help needed on Jane Street";


    public static String MESSAGE_REQUEST_SUCCESS = "Request for backup is successful.";

    private Msg requestHelpMessage;
    private WriteNotification writeNotification;

    public RequestHelpCommand(String caseName, String messageString) throws IllegalValueException {
        writeNotification = new WriteNotification(MessageFilePaths.FILEPATH_HQP_INBOX, true);
        requestHelpMessage = new Msg(Offense.getPriority(caseName), messageString, PatrolResourceStatus.getLocation(Password.getID()));
    }


    @Override
    public CommandResult execute() {
        try {
            writeNotification.writeToFile(requestHelpMessage);
            return new CommandResult(String.format(MESSAGE_REQUEST_SUCCESS));
        } catch (IOException ioe) {
            return new CommandResult(Messages.MESSAGE_SAVE_ERROR);
        }
    }


}
```
###### \seedu\addressbook\common\HttpRestClient.java
``` java
package seedu.addressbook.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * Http Client needed for RESTful APIs. Currently, only GET request is supported.
 */
public class HttpRestClient {
    private HttpClient httpClient;

    public HttpRestClient() {
        httpClient = HttpClientBuilder.create().build();
    }

    public HttpResponse requestGetResponse(String url) {
        try {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            return response;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }
}
```
###### \seedu\addressbook\common\Location.java
``` java
package seedu.addressbook.common;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Location {
    public static final String DISTANCE_MATRIX_BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    public static final String GOOGLE_MAPS_BASE_URL = "https://www.google.com/maps/place/";

    private String GOOGLE_MAPS_API_KEY;
    private double longitude;
    private double latitude;

    public Location(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        GOOGLE_MAPS_API_KEY = getGoogleMapsApiKey();
    }

    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public String getGoogleMapsApiKey()  {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("env"));
            return bufferedReader.readLine();
        } catch(IOException ioe) {
            ioe.getMessage();
        }

        return null;
    }

    private ArrayList<Pair<Integer, String>> sortEta(ArrayList<Pair<Integer, String>> etaList) {
        Collections.sort(etaList, Comparator.comparing(Pair::getValue0));
        return etaList;
    }

    /**
     * Returns Google Maps API URL for HTTP GET request from multiple origins.
     *
     * @param origins multiple origins ETA to this location
     * @return Google Maps URL String
     */
    private String getMapsDistanceUrl(ArrayList<Location> origins) {
        String originCoordinatesString = origins.get(0).latitude + "," + origins.get(0).longitude;

        for (int i = 1; i < origins.size(); i++) {
            originCoordinatesString += "|" + origins.get(i).latitude + "," + origins.get(i).longitude;
        }

        String googleEtaUrl = DISTANCE_MATRIX_BASE_URL + "origins=" + originCoordinatesString
                + "&destinations=" + this.latitude + "," + this.longitude + "&key=" + GOOGLE_MAPS_API_KEY;

        return googleEtaUrl;
    }

    /**
     * Returns List of Pairs of Estimated Time of Arrival (ETA) from JSON ETA data
     *
     * @param jsonData from Google Maps GET Request
     * @return ArrayList of Pair<ETA in seconds, ETA in natural text>
     */
    private ArrayList<Pair<Integer, String>> getEtaFromJsonObject(JSONObject jsonData) {
        ArrayList<Pair<Integer, String>> etaList = new ArrayList<>();

        try {
            JSONArray etaRows = jsonData.getJSONArray("rows");

            for (int i = 0; i < etaRows.length(); i++) {
                JSONArray EtaTimeElements = etaRows.getJSONObject(i).getJSONArray("elements");
                JSONObject etaDuration = EtaTimeElements.getJSONObject(0).getJSONObject("duration");

                Pair<Integer, String> durationPair = new Pair<>(etaDuration.getInt("value"), etaDuration.getString("text"));
                etaList.add(durationPair);
            }

            return etaList;
        } catch(JSONException jsonE) {
            jsonE.printStackTrace();
        }

        return etaList;
    }

    /**
     * Returns ETA for this location from multiple locations
     *
     * @param locations list of origins to this destination
     * @return ArrayList of Pair<Number of seconds ETA, Text description of ETA>
     */

    public ArrayList<Pair<Integer, String>> getEtaFrom(ArrayList<Location> locations) {
        ArrayList<Pair<Integer, String>> etaList = new ArrayList<>();

        try {
            HttpRestClient httpRestClient = new HttpRestClient();
            HttpResponse response = httpRestClient.requestGetResponse(getMapsDistanceUrl(locations));

            if (response != null) {
                String jsonString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
                etaList = getEtaFromJsonObject(new JSONObject(jsonString));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sortEta(etaList);
    }

    public String getGoogleMapsURL() {
        return GOOGLE_MAPS_BASE_URL + this.getLatitude() + "," + this.getLongitude();
    }

    /*public static void main(String[] args) {
        Location location = new Location(-6.206968,106.751365);
        Location origin = new Location(-6.189482, 106.733902);
        ArrayList<Location> locationList = new ArrayList<>();
        locationList.add(origin);

        ArrayList<Pair<Integer, String>> ETATiming = location.getEtaFrom(locationList);
        for (Pair<Integer, String> eta: ETATiming) {
            System.out.println(eta.getValue0() + " " + eta.getValue1());
        }
    }*/
}
```
###### \seedu\addressbook\data\person\Offense.java
``` java
    private static HashMap<String, Msg.Priority> OFFENSE_LIST = new HashMap<>();
    static {
        OFFENSE_LIST.put("none", Msg.Priority.LOW);
        OFFENSE_LIST.put("theft", Msg.Priority.HIGH);
        OFFENSE_LIST.put("drugs", Msg.Priority.LOW);
        OFFENSE_LIST.put("riot", Msg.Priority.HIGH);
        OFFENSE_LIST.put("murder", Msg.Priority.MED);
        OFFENSE_LIST.put("fleeing suspect", Msg.Priority.MED);
        OFFENSE_LIST.put("gun", Msg.Priority.HIGH);
        OFFENSE_LIST.put("theft1", Msg.Priority.HIGH);
        OFFENSE_LIST.put("theft2", Msg.Priority.HIGH);
        OFFENSE_LIST.put("theft3", Msg.Priority.HIGH);
        OFFENSE_LIST.put("theft4", Msg.Priority.HIGH);
    }
```
###### \seedu\addressbook\data\person\Offense.java
``` java
    /**
     * Returns priority for a given offense.
     *
     * @params offense name
     * @return priority
     */
    public static Msg.Priority getPriority(String offense) throws IllegalValueException {
        offense = offense.toLowerCase();
        if (!OFFENSE_LIST.containsKey(offense)) {
            throw new IllegalValueException("Offense does not exist in database.");
        }

        return OFFENSE_LIST.get(offense);
    }

    public static int getPriority(Msg.Priority priority) {
        return priority.toInteger();
    }
```
###### \seedu\addressbook\parser\Parser.java
``` java

    /**
     * Parses arguments in the context of the edit person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareEdit(String args) {
        final Matcher matcher = EDIT_DATA_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        try {
            return new EditCommand(
                    matcher.group("nric"),
                    matcher.group("postalCode"),
                    matcher.group("status"),
                    matcher.group("wantedFor"),
                    getTagsFromArgs(matcher.group("pastOffenseArguments"))
            );
        } catch (IllegalValueException ive) {
            logr.log(Level.WARNING, "Invalid edit command format.", ive);
            return new IncorrectCommand(ive.getMessage());
        }
    }

```
###### \seedu\addressbook\parser\Parser.java
``` java

    /**
     * Parses arguments in context of request help command.
     *
     * @param args full command args string
     * @return the prepared request command
     */
    private Command prepareRequest(String args) {
        String caseName, message;
        String[] argParts = args.trim().split(" ", 2);

        if (argParts.length < 2) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RequestHelpCommand.MESSAGE_USAGE));
        }

        caseName = argParts[0];
        message = argParts[1];

        try {
            return new RequestHelpCommand(caseName, message);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(Offense.MESSAGE_OFFENSE_INVALID);
        }
    }

```
###### \seedu\addressbook\parser\Parser.java
``` java
    /**
     * Parses arguments in context of dispatch command
     *
     * @params args full command args string
     * @return the prepared request command
     */
    private Command prepareDispatch(String args) {
        String backupOfficer, dispatchRequester, caseName;
        String[] argParts = args.trim().split(" ",  3);

        if (argParts.length < 3) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DispatchBackup.MESSAGE_USAGE));
        }

        backupOfficer = argParts[0].toLowerCase();
        caseName = argParts[1].toLowerCase();
        dispatchRequester = argParts[2].toLowerCase();

        return new DispatchBackup(backupOfficer, dispatchRequester, caseName);
    }

}
```
###### \seedu\addressbook\PatrolResourceStatus.java
``` java
package seedu.addressbook;

import org.javatuples.Triplet;
import seedu.addressbook.common.Location;

import java.util.ArrayList;

public class PatrolResourceStatus {
    // Triplet<Police Officer ID, Location, isEngaged
    private static ArrayList<Triplet<String, Location, Boolean>> patrolResourceStatus = new ArrayList<>(){{
        add( new Triplet<>("hqp", new Location(1.294166, 103.770730), false) ); // NUS FASS
        add( new Triplet<>("po1", new Location(1.306935, 103.790564), false) ); // Buona Vista
        add( new Triplet<>("po2", new Location(1.346301, 103.682060), false) ); // NTU
        add( new Triplet<>("po3", new Location(1.296057, 103.849865), false) ); // SMU
        add( new Triplet<>("po4", new Location(1.340352, 103.962193), false) ); // SUTD
        add( new Triplet<>("po5", new Location(1.329393, 103.776169), false) ); // SIM
    }};

    public static ArrayList<Triplet<String, Location, Boolean>> getPatrolResourceStatus() {
        return patrolResourceStatus;
    }

    // TODO: put into message
    public static Location getLocation(String patrolResource) {
        switch (patrolResource) {
            case "hqp":
                return patrolResourceStatus.get(0).getValue1();
            case "po1":
                return patrolResourceStatus.get(1).getValue1();
            case "po2":
                return patrolResourceStatus.get(2).getValue1();
            case "po3":
                return patrolResourceStatus.get(3).getValue1();
            case "po4":
                return patrolResourceStatus.get(4).getValue1();
            case "po5":
                return patrolResourceStatus.get(5).getValue1();
            default:
                return null;
        }
    }

    public static void setStatus(String policeOfficerId, Boolean status) {
        for (Triplet<String, Location, Boolean> policeOfficer : patrolResourceStatus) {
            if (policeOfficer.getValue0().equalsIgnoreCase(policeOfficerId)) {
                policeOfficer.setAt2(status);
                return;
            }
        }
    }

}
```
