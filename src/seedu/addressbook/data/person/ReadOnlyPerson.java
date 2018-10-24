package seedu.addressbook.data.person;

import java.util.Set;


/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyPerson {

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
