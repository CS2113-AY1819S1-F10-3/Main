package seedu.addressbook.data.person;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import seedu.addressbook.data.tag.Tag;

/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyPerson {

    Name getName();
    NRIC getNRIC();
    DateOfBirth getDateOfBirth();
    PostalCode getPostalCode();
    Status getStatus();
    Offense getWantedFor();

    /**
     * The returned {@code Set} is a deep copy of the internal {@code Set},
     * changes on the returned list will not affect the person's internal tags.
     */
    Set<Offense> getPastOffense();

    /**
     * Returns true if the values inside this object is same as those of the other (Note: interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyPerson other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().fullName.equals(this.getName().fullName) // state checks here onwards
                && other.getNRIC().getIdentificationNumber().equals(this.getNRIC().getIdentificationNumber())
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
        builder.append(getNRIC())
                .append(" DateOfBirth: ");
        builder.append(getDateOfBirth().getDOB())
                .append(" Postal Code: ");
        builder.append(getPostalCode())
                .append(" Status: ");
        builder.append(getStatus())
                .append(" Wanted For: ");
        builder.append(getWantedFor())
                .append(" Past Offences:");
        for (Offense offense : getPastOffense()) {
            builder.append(offense);
        }
        return builder.toString();
    }



}
