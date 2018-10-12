package seedu.addressbook.data.person;

import java.sql.Time;
import java.util.*;

import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.tag.Tag;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Represents a Person in the system.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Person implements ReadOnlyPerson {

    private Name name;
    private NRIC nric;
    private DateOfBirth dateOfBirth;
    private PostalCode postalCode;
    private Status status;
    private Offense wantedFor;

    private final Set<Offense> PastOffense = new HashSet<>();

    public static String WANTED_FOR_WARNING = "State the offence if person's status is wanted";

    private Set<Timestamp> screeningHistory;



    /**
     * Assumption: Every field must be present and not null.
     */
    public Person(Name name, NRIC nric, DateOfBirth dateOfBirth, PostalCode postalCode, Status status ,
                  Offense wantedFor, Set<Offense> PastOffense) throws IllegalValueException {
        this.name = name;
        this.nric = nric;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
        this.status = status;
        this.wantedFor = wantedFor;
        if ((this.status.getCurrentStatus().equals(this.status.WANTED_KEYWORD)) && ((this.wantedFor.getOffense().equals(this.wantedFor.NULL_OFFENSE)) ||
                this.wantedFor == null)){
            throw new IllegalValueException(WANTED_FOR_WARNING);
        }

        else if (!(this.status.getCurrentStatus().equals(this.status.WANTED_KEYWORD))){
            this.wantedFor = new Offense();

        }

        else{

            this.wantedFor = wantedFor;
        }
        this.PastOffense.addAll(PastOffense);
    }

    /**
     * Copy constructor.
     */
    public Person(ReadOnlyPerson source) throws IllegalValueException {
        this(source.getName(), source.getNRIC(),
                source.getDateOfBirth(), source.getPostalCode(), source.getStatus(),
                source.getWantedFor(), source.getPastOffense());
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public NRIC getNRIC() {
        return nric;
    }

    @Override
    public DateOfBirth getDateOfBirth() {return dateOfBirth;}

    @Override
    public PostalCode getPostalCode() {
        return postalCode;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Offense getWantedFor() {
        return wantedFor;
    }

    @Override
    public Set<Offense> getPastOffense() {return PastOffense;}

    @Override
    public Set<Timestamp> getScreeningHistory() {return screeningHistory;}

    /**
     * Replaces this person's tags with the tags in {@code replacement}.
     */
    public void setPastOffense(Set<Offense> replacement) {
        PastOffense.clear();
        PastOffense.addAll(replacement);
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
        return Objects.hash(name, nric, dateOfBirth, postalCode, status, wantedFor, PastOffense);
    }

    @Override
    public String toString() {
        return getAsTextShowAll();
    }

}
