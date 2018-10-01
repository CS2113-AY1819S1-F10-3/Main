package seedu.addressbook.data.person;

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
                && other.getName().equals(this.getName()) // state checks here onwards
                && other.getNRIC().equals(this.getNRIC())
                && other.getDateOfBirth().equals((this.getDateOfBirth()))
                && other.getPostalCode().equals(this.getPostalCode())
                && other.getStatus().equals(this.getStatus())
                && other.getWantedFor().equals(this.getWantedFor()));
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

    /**
     * Formats a person as text, showing only non-private contact details.
     */
    /**
    default String getAsTextHidePrivate() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" NRIC: ");
        builder.append(getNRIC())
                .append(" DateOfBirth: ");
        builder.append(getDateOfBirth())
                .append(" Postal Code");
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
     */
}
