package seedu.addressbook.data.person;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.addressbook.data.tag.Tag;

/**
 * Represents a Person in the system.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Person implements ReadOnlyPerson {

    private Name name;
    private NRIC nric;
    private Status status;
    private PostalCode postalCode;

    private final Set<Offense> PastOffense = new HashSet<>();
    /**
     * Assumption: Every field must be present and not null.
     */
    public Person(Name name, NRIC nric, PostalCode postalCode, Status status ,Set<Offense> PastOffense) {
        this.name = name;
        this.nric = nric;
        this.postalCode = postalCode;
        this.status = status;
        this.PastOffense.addAll(PastOffense);
    }

    /**
     * Copy constructor.
     */
    public Person(ReadOnlyPerson source) {
        this(source.getName(), source.getNRIC(), source.getPostalCode(), source.getStatus(), source.getPastOffense());
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
    public PostalCode getPostalCode() {
        return postalCode;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Set<Offense> getPastOffense() {return PastOffense;}

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
        return Objects.hash(name, nric, postalCode, status, PastOffense);
    }

    @Override
    public String toString() {
        return getAsTextShowAll();
    }

}
