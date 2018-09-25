package seedu.addressbook.data.PoliceOfficers;


/**
 * A read-only immutable interface for a Patrol Resource  in the system.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyPatrolResource {

    PatrolID getPatrolID();
    State getState();
    Case getCurrentCase();
    Case RA(); //request ambulance
    Case RB(); //request backup, can be other POs or can be enhanced like SWAT
    Case RF(); //request fire fighters


    /**
     * Returns true if the values inside this object is same as those of the other (Note: interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyPatrolResource other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getPatrolID().equals(this.getPatrolID()) // state checks here onwards
                && other.getState().equals(this.getState())
                && other.getCurrentCase().equals(this.getCurrentCase()));

    }


    /**
     * Formats a patrol resource as text, showing all details.
     */
    default String getAsTextHidePrivate() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Patrol ID: ").append(getPatrolID());
        builder.append(" State: ").append(getState());
        builder.append(" Current Case: ").append(getCurrentCase());

        return builder.toString();
    }
}
