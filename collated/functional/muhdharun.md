# muhdharun
###### \seedu\addressbook\commands\CheckCommand.java
``` java
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckCommand extends Command {

    public static final String COMMAND_WORD = "check";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Gets screening history of person with specified NRIC \n\t"
            + "Parameters: NRIC ...\n\t"
            + "Example: " + COMMAND_WORD + " s1234567a";
    private String nricKeyword;


    public CheckCommand(String nricToFind)
    {
        this.nricKeyword = nricToFind;
    }

    public String getNricKeyword(){
        return nricKeyword;
    }

    @Override
    public CommandResult execute() {
        final List<String> screeningHistory = getPersonWithNric(nricKeyword);
        return new CommandResult(getMessageForScreeningHistoryShownSummary(screeningHistory,nricKeyword));
    }

    private List<String> getPersonWithNric(String nric){
        List<String> screeningHistory = new ArrayList<>();
        try {
            screeningHistory = addressBook.readDatabase(nric);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screeningHistory;
    }

}
```
###### \seedu\addressbook\commands\CheckPOStatusCommand.java
``` java
import org.javatuples.Triplet;
import seedu.addressbook.PatrolResourceStatus;
import seedu.addressbook.common.Location;

import java.util.ArrayList;
import java.util.List;

public class CheckPOStatusCommand extends Command {

    public static final String COMMAND_WORD = "checkstatus";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Gets current status of all POs \n\t"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        List<String> allPos = new ArrayList<>();
        ArrayList<Triplet<String, Location, Boolean>> pos = PatrolResourceStatus.getPatrolResourceStatus();
        for (int i = 0 ; i < pos.size() ; i++){
            String poStatus = pos.get(i).getValue0() + " " + pos.get(i).getValue2();
            allPos.add(poStatus);
        }
        return new CommandResult(getMessage(allPos));
    }

}
```
###### \seedu\addressbook\commands\Command.java
``` java
    public static String getMessageForPersonShownSummary(ReadOnlyPerson personDisplayed) {
        if (personDisplayed == null){
            return Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK;
        }
        else{
            String result = personDisplayed.getAsTextShowAllInVerticalMode();
            return result + "\n\n" + String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        }
    }

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of persons.
     *
     * @param timestampsDisplayed used to generate summary
     * @return summary message for timestamps displayed
     */

    public static String getMessageForScreeningHistoryShownSummary(List<String> timestampsDisplayed, String nric) {

        Formatter formatter = new Formatter();
        String result = formatter.formatForStrings(timestampsDisplayed);

        String finalResult = result + String.format(Messages.MESSAGE_TIMESTAMPS_LISTED_OVERVIEW, nric, timestampsDisplayed.size());
        return finalResult;
    }

    public static String getMessage(List<String> args) {

        Formatter formatter = new Formatter();
        String result = formatter.formatForStrings(args);

        return result;

    }

    //@@ author
    /**
     * Executes the command and returns the result.
     */
    public CommandResult execute() throws IllegalValueException {
        throw new UnsupportedOperationException("This method should be implement in child classes");
    }

    //Note: it is better to make the execute() method abstract, by replacing the above method with the line below:
    //public abstract CommandResult execute();

    /**
     * Supplies the data the command will operate on.
     */
    public void setData(AddressBook addressBook, List<? extends ReadOnlyPerson> relevantPersons) {
        this.addressBook = addressBook;
        this.relevantPersons = (relevantPersons.isEmpty()) ? addressBook.getAllPersons().immutableListView() : relevantPersons;
    }

    /**
     * Extracts the the target person in the last shown list from the given arguments.
     *
     * @throws IndexOutOfBoundsException if the target index is out of bounds of the last viewed listing
     */
    protected ReadOnlyPerson getTargetPerson() throws IndexOutOfBoundsException {
        return relevantPersons.get(getTargetIndex() - DISPLAYED_INDEX_OFFSET);
    }

```
###### \seedu\addressbook\commands\DeleteCommand.java
``` java
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" 
            + "Deletes the person by nric.\n\t"
            + "Parameters: NRIC\n\t"
            + "Example: " + COMMAND_WORD + " s1234567a";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private NRIC toDelete;

    public DeleteCommand(NRIC nric) {
        this.toDelete = nric;
    }


    @Override
    public CommandResult execute() {
        try {
            final ReadOnlyPerson target = (toDelete == null) ? getTargetPerson() : getTargetPerson(toDelete);
            addressBook.removePerson(target);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, target));
        } catch (IndexOutOfBoundsException ie) {
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } catch (PersonNotFoundException pnfe) {
```
###### \seedu\addressbook\commands\FindCommand.java
``` java
    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Finds person with specified NRIC \n\t"
            + "Parameters: NRIC ...\n\t"
            + "Example: " + COMMAND_WORD + " s1234567a";

    private String nric;

    public FindCommand(String nricToFind) {
        this.nric = nricToFind;
    }

    public String getNric(){
        return nric;
    }

    @Override
    public CommandResult execute() {
        try {
            final ReadOnlyPerson personFound = getPersonWithNric();
            return new CommandResult(getMessageForPersonShownSummary(personFound));
        } catch(IOException ioe) {
            return new CommandResult("Cannot find person with nric");
        }
    }


    /**
     * Retrieve all persons in the address book whose names contain some of the specified keywords.
     *
     * @return Persons found, null if no person found
     */
    public ReadOnlyPerson getPersonWithNric() throws IOException {
        for (ReadOnlyPerson person : relevantPersons) {
            if (person.getNric().getIdentificationNumber().equals(nric)) {
                addressBook.addPersonToDbAndUpdate(person);
                addressBook.updateDatabase();
                return person;
            }
        }

        return null;
    }

}
```
###### \seedu\addressbook\commands\UpdateStatusCommand.java
``` java

import seedu.addressbook.PatrolResourceStatus;

import static seedu.addressbook.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class UpdateStatusCommand extends Command {

    public static final String COMMAND_WORD = "updatestatus";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Updates the 'isEngaged' status of PO to false \n\t"
            + "Parameters: PO ID\n\t"
            + "Example: " + COMMAND_WORD + " po2";

    private String toUpdate;

    public static final String MESSAGE_UPDATE_PO_SUCCESS = "%s is now free (not engaged)";

    public UpdateStatusCommand(String po) {this.toUpdate = po;}

    @Override
    public CommandResult execute() {
        PatrolResourceStatus.setStatus(toUpdate,false);

        return new CommandResult(String.format(MESSAGE_UPDATE_PO_SUCCESS,toUpdate));
    }

}
```
###### \seedu\addressbook\data\AddressBook.java
``` java
    public void addPersonToDbAndUpdate(ReadOnlyPerson toAdd) {
        TimeAndDate timeAndDate = new TimeAndDate();
        tempNric = toAdd.getNric().getIdentificationNumber();
        tempTimestamp = timeAndDate.outputDATHrs();
    }

    public List<String> readDatabase(String nric) throws IOException {
        List<String> data = new ArrayList<>();
        String line;
        BufferedReader br = readerAndWriter.openReader(databaseFile);
        line = br.readLine();
        while (line != null){
            String[] parts = line.split(" ");

            if (parts[0].equals(nric)){
                if(parts[2].equals("null")){
                    continue;
                }
                data.add(parts[1] + " by " + parts[2]);
                line = br.readLine();
            }
            else{
                line = br.readLine();
            }
        }
        br.close();
        return data;
    }

    public void updateDatabase() throws IOException {
        String line;
        BufferedReader br = readerAndWriter.openReader(databaseFile);
        FileWriter write = new FileWriter(SCREENING_DATABASE,true);
        PrintWriter myPrinter = new PrintWriter(write);
        try {
            while ((line = br.readLine()) !=  null){
                String[] parts = line.split(" ");
                if (parts[0].equals(tempNric)){
                    myPrinter.println(tempNric + " " + tempTimestamp + " " + Password.getID());
                    myPrinter.close();
                    br.close();
                    return;
                }
                line = br.readLine();
                continue;
            }
            myPrinter.println(tempNric + " " + tempTimestamp + " " + Password.getID());
            myPrinter.close();
            br.close();
        }
        catch (Exception e){
            myPrinter.print(tempNric + " " + tempTimestamp + " " + Password.getID());

            myPrinter.close();
            br.close();
        }
    }
```
###### \seedu\addressbook\data\person\DateOfBirth.java
``` java
import seedu.addressbook.data.exception.IllegalValueException;

import java.util.Calendar;

/**
 * Represents a Person's Date of Birth in the EX-SI-53.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateOfBirth(String)}
 */

public class DateOfBirth {
    public static final String EXAMPLE = "1996";
    public static final String MESSAGE_DATE_OF_BIRTH_CONSTRAINTS = "DoB must from 1900 onwards, and less than or equal to current year";
    private static final String DATE_OF_BIRTH_VALIDATION_REGEX =  "[1-2][0-9]{3}";
    private final int year = Calendar.getInstance().get(Calendar.YEAR);
    private final String birthYear;




    /**
     * Validates given DoB.
     *
     * @throws IllegalValueException if given DoB string is invalid.
     */

    public DateOfBirth(String dob) throws IllegalValueException{
        dob = dob.trim();
        if (!isValidDateOfBirth(dob)){
            throw new IllegalValueException(MESSAGE_DATE_OF_BIRTH_CONSTRAINTS);
        }
        this.birthYear = dob;

    }


    public String getDOB() {
        return birthYear;
    }

    public boolean isValidDateOfBirth(String test) {return test.matches(DATE_OF_BIRTH_VALIDATION_REGEX) &&
            Integer.parseInt(test) <= year;}



}
```
###### \seedu\addressbook\data\person\NRIC.java
``` java
import seedu.addressbook.data.exception.IllegalValueException;

/**
 * Represents a Person's identification number(NRIC or FIN) in the EX-SI-53.
 * Guarantees: immutable; is valid as declared in {@link #isValidNRIC(String)}
 */

public class NRIC {
    public static final String EXAMPLE = "s1234567a";
    public static final String MESSAGE_NAME_CONSTRAINTS = "NRIC/FIN should start with 's'/'t'/'g'/'f'(lower case) and end with a letter and " +
            "must have 7 digits in between, no spaces";
    private static final String NAME_VALIDATION_REGEX = "[stgf][0-9]{7}[a-z]";

    private final String identificationNumber;

    /**
     * Validates given nric.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public NRIC(String nric) throws IllegalValueException {
        nric = nric.trim();
        if (!isValidNRIC(nric)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.identificationNumber = nric;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    /**
     * Returns true if a given string is a valid NRIC.
     */
    public static boolean isValidNRIC(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return identificationNumber;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NRIC // instanceof handles nulls
                && this.identificationNumber.equals(((NRIC) other).identificationNumber)); // state check
    }

    @Override
    public int hashCode() {
        return identificationNumber.hashCode();
    }
}
```
###### \seedu\addressbook\data\person\Offense.java
``` java
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.inbox.Msg;

import java.util.*;

public class Offense {
    public static final String EXAMPLE = "theft";
    public static final String MESSAGE_OFFENSE_INVALID = "Offense must be inside the list";
    public static final String NULL_OFFENSE = "none";
```
###### \seedu\addressbook\data\person\Offense.java
``` java
    private final String offense;

    public Offense(){
        this.offense = "none";
    }

    /**
     * Validates given tag name.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Offense(String offense) throws IllegalValueException {
        offense = offense.toLowerCase().trim();

        if (!isValidOffense(offense)) {
            throw new IllegalValueException(MESSAGE_OFFENSE_INVALID);
        }

        this.offense = offense;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidOffense(String offense) {
        return OFFENSE_LIST.containsKey(offense.toLowerCase());
    }

    public String getOffense() {
        return offense;
    }
```
###### \seedu\addressbook\data\person\Offense.java
``` java
    public static Set<Offense> getOffenseSet(Set<String> offenseStringSet) throws IllegalValueException {
        Set<Offense> offenseSet = new HashSet<>();

        for (String offense: offenseStringSet) {
            offenseSet.add(new Offense(offense));
        }

        return offenseSet;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Offense // instanceof handles nulls
                && this.offense.equals(((Offense) other).offense)); // state check
    }

    @Override
    public int hashCode() {
        return offense.hashCode();
    }

    @Override
    public String toString() {
        return '[' + offense + ']';
    }
}
```
###### \seedu\addressbook\data\person\Person.java
``` java
public class Person implements ReadOnlyPerson {

    private Name name;
    private NRIC nric;
    private DateOfBirth dateOfBirth;
    private PostalCode postalCode;
    private Status status;
    private Offense wantedFor;

    private Set<Offense> pastOffenses = new HashSet<>();

    public static String WANTED_FOR_WARNING = "State the offence if person's status is wanted";


    /**
     * Assumption: Every field must be present and not null.
     */
    public Person(Name name, NRIC nric, DateOfBirth dateOfBirth, PostalCode postalCode, Status status ,
                  Offense wantedFor, Set<Offense> PastOffenses) throws IllegalValueException {
        this.name = name;
        this.nric = nric;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
        this.status = status;
        this.wantedFor = wantedFor;
        if ((this.status.getCurrentStatus().equals(Status.WANTED_KEYWORD)) && ((this.wantedFor.getOffense().equals(Offense.NULL_OFFENSE)) ||
                this.wantedFor == null)){
            throw new IllegalValueException(WANTED_FOR_WARNING);
        }

        else if (!(this.status.getCurrentStatus().equals(Status.WANTED_KEYWORD))){

        } else if (!(this.status.getCurrentStatus().equals(this.status.WANTED_KEYWORD))){

            this.wantedFor = new Offense();
        } else{
            this.wantedFor = wantedFor;
        }

        this.pastOffenses.addAll(PastOffenses);
    }

    /**
     * Copy constructor.
     */
    public Person(ReadOnlyPerson source) throws IllegalValueException {
        this(source.getName(), source.getNric(),
                source.getDateOfBirth(), source.getPostalCode(), source.getStatus(),
                source.getWantedFor(), source.getPastOffenses());
    }



    @Override
    public Name getName() {
        return name;
    }

    @Override
    public NRIC getNric() {
        return nric;
    }

    @Override
    public DateOfBirth getDateOfBirth() {return dateOfBirth;}

    @Override
    public PostalCode getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(PostalCode postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Offense getWantedFor() {
        return wantedFor;
    }
    public void setWantedFor(Offense wantedFor) {
        this.wantedFor = wantedFor;
    }


    @Override
    public Set<Offense> getPastOffenses() {
        return pastOffenses;
    }

    public Set<String> getStringOffenses() {
        Set<String> offenseStringSet = new HashSet<>();
        for (Offense offense: this.pastOffenses) {
            offenseStringSet.add(offense.getOffense());
        }
        return offenseStringSet;
    }

    /**
     * Replaces this person's tags with the tags in {@code newPastOffenses}.
     */
    public void addPastOffenses(Set<Offense> newPastOffenses) {
        pastOffenses.addAll(newPastOffenses);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyPerson // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyPerson) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, nric, dateOfBirth, postalCode, status, wantedFor, pastOffenses);
    }

    @Override
    public String toString() {
        return getAsTextShowAll();
    }

}
```
###### \seedu\addressbook\data\person\PostalCode.java
``` java
import seedu.addressbook.data.exception.IllegalValueException;

/**
 * Represents a Person's Postal Code in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPostalCode(String)}
 */

public class PostalCode {

    public static final String EXAMPLE = "510123";
    public static final String MESSAGE_NAME_CONSTRAINTS = "Postal Code must be 6 digits long";
    public static final String NAME_VALIDATION_REGEX = "[0-9]{6}";

    public final String postalCode;

    /**
     * Validates given Postal Code.
     *
     * @throws IllegalValueException if given Postal Code string is invalid.
     */
    public PostalCode(String pc) throws IllegalValueException {
        pc = pc.trim();
        if (!isValidPostalCode(pc)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.postalCode = pc;
    }

    public String getPostalCode(){
        return postalCode;
    }

    /**
     * Returns true if a given string is a valid NRIC.
     */
    public static boolean isValidPostalCode(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return postalCode;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PostalCode // instanceof handles nulls
                && this.postalCode.equals(((PostalCode) other).postalCode)); // state check
    }

    @Override
    public int hashCode() {
        return postalCode.hashCode();
    }

}
```
###### \seedu\addressbook\data\person\ReadOnlyPerson.java
``` java
    Name getName();
    NRIC getNric();
    DateOfBirth getDateOfBirth();
    PostalCode getPostalCode();
    Status getStatus();
    Offense getWantedFor();

    /**
     * The returned {@code Set} is a deep copy of the internal {@code Set},
     * changes on the returned list will not affect the person's internal tags.
     */
    Set<Offense> getPastOffenses();

    /**
     * TODO: Feel in command
     */
    Set<String> getStringOffenses();

    /**
     * Returns true if the values inside this object is same as those of the other (Note: interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyPerson other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().fullName.equals(this.getName().fullName) // state checks here onwards
                && other.getNric().getIdentificationNumber().equals(this.getNric().getIdentificationNumber())
                && other.getDateOfBirth().getDOB().equals((this.getDateOfBirth().getDOB()))
                && other.getPostalCode().getPostalCode().equals(this.getPostalCode().getPostalCode())
                && other.getStatus().getCurrentStatus().equals(this.getStatus().getCurrentStatus())
                && other.getWantedFor().getOffense().equals(this.getWantedFor().getOffense()));
    }

    /**
     * Formats the person as text, showing all contact details.
     */
    default String getAsTextShowAll() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" NRIC: ");
        builder.append(getNric())
                .append(" DateOfBirth: ");
        builder.append(getDateOfBirth().getDOB())
                .append(" Postal Code: ");
        builder.append(getPostalCode())
                .append(" Status: ");
        builder.append(getStatus())
                .append(" Wanted For: ");
        builder.append(getWantedFor())
                .append(" Past Offences:");
        for (Offense offense : getPastOffenses()) {
            builder.append(offense);
        }
        return builder.toString();
    }
```
###### \seedu\addressbook\data\person\ReadOnlyPerson.java
``` java
    default String getAsTextShowAllInVerticalMode() {

        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("\n")
                .append(" NRIC: ");
        builder.append(getNric())
                .append("\n")
                .append(" DateOfBirth: ");
        builder.append(getDateOfBirth().getDOB())
                .append("\n")
                .append(" Postal Code: ");
        builder.append(getPostalCode())
                .append("\n")
                .append(" Status: ");
        builder.append(getStatus())
                .append("\n")
                .append(" Wanted For: ");
        builder.append(getWantedFor())
                .append("\n")
                .append(" Past Offences:");
        for (Offense offense : getPastOffenses()) {
            builder.append(offense);
        }
        return builder.toString();
    }



}
```
###### \seedu\addressbook\data\person\Status.java
``` java
import seedu.addressbook.data.exception.IllegalValueException;

import java.util.Arrays;

/**
 * Represents a Person's criminal status (if any) in EX-SI-53.
 * Guarantees: mutable; is valid as declared in {@link #isValidStatus(String)}
 */

public class Status {

    public static final String EXAMPLE = "wanted";
    private static final String MESSAGE_NAME_CONSTRAINTS = "Status should be one of the 3: wanted/xc/clear";

    public static final String WANTED_KEYWORD = "wanted";
    public static final String EXCONVICT_KEYWORD = "xc"; //ex-convict
    public static final String CLEAR_KEYWORD = "clear";

    private static final String[] STATUS_VALIDATION = {WANTED_KEYWORD,EXCONVICT_KEYWORD,CLEAR_KEYWORD};

    private final String currentStatus;

    /**
     * Validates given Status.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public Status(String status) throws IllegalValueException {
        status = status.trim();
        if (!isValidStatus(status)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.currentStatus = status;
    }

    public String getCurrentStatus() {return currentStatus;}

    /**
     * Returns true if a given string is a valid Status.
     */

    private static boolean isValidStatus(String test) {

        return Arrays.asList(STATUS_VALIDATION).contains(test);
    }

    @Override
    public String toString() {
        return currentStatus;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Status // instanceof handles nulls
                && this.currentStatus.equals(((Status) other).currentStatus)); // state check
    }

    @Override
    public int hashCode() {
        return currentStatus.hashCode();
    }

}
```
###### \seedu\addressbook\data\person\UniquePersonList.java
``` java
    public static class DuplicateNricException extends DuplicateDataException {
        protected DuplicateNricException() {
            super("Operation would result in duplicate NRIC");
        }
    }
```
###### \seedu\addressbook\data\person\UniquePersonList.java
``` java
    public boolean containNric(Person toCheck) {
        for ( Person person : internalList){
            if (person.getNric().getIdentificationNumber().equals(toCheck.getNric().getIdentificationNumber())){
                return true;
            }
        }
        return false;
    }
```
###### \seedu\addressbook\parser\Parser.java
``` java
    public static final Pattern PERSON_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<name>[^/]+)"
                    + " n/(?<nric>[^/]+)"
                    + " d/(?<dateOfBirth>[^/]+)"
                    + " p/(?<postalCode>[^/]+)"
                    + " s/(?<status>[^/]+)"
                    + " w/(?<wantedFor>[^/]+)"
                    + "(?<pastOffenseArguments>(?: o/[^/]+)*)"); // variable number of offenses
```
###### \seedu\addressbook\parser\Parser.java
``` java
    /**
     * Parses arguments in the context of the add person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
        final Matcher matcher = PERSON_DATA_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        try {
            return new AddCommand(
                    matcher.group("name"),
                    matcher.group("nric"),
                    matcher.group("dateOfBirth"),
                    matcher.group("postalCode"),
                    matcher.group("status"),
                    matcher.group("wantedFor"),
                    getTagsFromArgs(matcher.group("pastOffenseArguments"))
            );
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
```
###### \seedu\addressbook\parser\Parser.java
``` java

    /**
     * Parses arguments in the context of the delete person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args){
        try {
            final String nric = parseArgsAsNric(args);

            return new DeleteCommand(new NRIC(nric));
        } catch (ParseException e) {

            logr.log(Level.WARNING, "Invalid delete command format.", e);
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));



        } catch (IllegalValueException ive) {
            logr.log(Level.WARNING, "Invalid name/id inputted.", ive);
            return new IncorrectCommand(ive.getMessage());
        }
    }


```
###### \seedu\addressbook\parser\Parser.java
``` java
    private String parseArgsAsNric(String args) throws ParseException {
        final Matcher matcher = PERSON_NRIC_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            logr.warning("NRIC does not exist in argument");
            throw new ParseException("Could not find NRIC to parse");
        }
        return matcher.group(0);
    }

    /**
     * Parses arguments in the context of the check or find person command.
     *
     * @param args full command args string
     * @return the prepared command
     */

    private Command prepareCheck(String args) {
        args = args.trim();
        if (NRIC.isValidNRIC(args)) {
            return new CheckCommand(args);
        } else {
            logr.warning("NRIC argument is invalid");
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,CheckCommand.MESSAGE_USAGE));
        }
    }

    private Command prepareFind(String args) {
        args = args.trim();
        if (NRIC.isValidNRIC(args)) {
            return new FindCommand(args);
        } else {
            logr.warning("NRIC argument is invalid");
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    private Command prepareUpdateStatus(String args) {
        args = args.trim();
        if (!args.equals("")) {
            return new UpdateStatusCommand(args);
        } else{
            logr.warning("PO must be stated");
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateStatusCommand.MESSAGE_USAGE));
        }

    }

```
