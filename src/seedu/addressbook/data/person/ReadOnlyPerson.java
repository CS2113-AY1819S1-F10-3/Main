package seedu.addressbook.data.person;

import java.util.Set;

import seedu.addressbook.data.tag.Tag;

/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyPerson {

    Name getName();
    NRIC getNRIC();
    PostalCode getPostalCode();
    Status getStatus();

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
                && other.getPostalCode().equals(this.getPostalCode())
                && other.getStatus().equals(this.getStatus()));
    }

    /**
     * Formats the person as text, showing all contact details.
     */
    default String getAsTextShowAll() {
        final StringBuilder builder = new StringBuilder();
        final String detailIsPrivate = "(private) ";
        builder.append(getName())
                .append(" NRIC: ");
        builder.append(getNRIC())
                .append(" Email: ");
        builder.append(getPostalCode())
                .append(" Postal Code: ");
        builder.append(getPostalCode())
                .append(" Past Offenses: ");
        for (Offense offense : getPastOffense()) {
            builder.append(offense);
        }
        return builder.toString();
    }

    /**
     * Formats a person as text, showing only non-private contact details.
     */
    default String getAsTextHidePrivate() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName());
        builder.append(" NRIC: ").append(getNRIC());
        builder.append(" Postal Code: ").append(getPostalCode());
        builder.append(" Status: ").append(getStatus());
        builder.append(" Past Offenses: ");
        for (Offense offense : getPastOffense()) {
            builder.append(offense);
        }
        return builder.toString();
    }
}
