package seedu.addressbook.data.PoliceOfficers;

import seedu.addressbook.data.exception.IllegalValueException;

/**
 * Represents a Patrol resource's ID in EX-SI-53.
 * Guarantees: immutable; is valid as declared in {@link #isValidID(int)}
 */

public class PatrolID {
    public static final String EXAMPLE = "PO1";
    public static final String MESSAGE_ID_CONSTRAINTS = "Patrol ID has to be a number excluding 0, and must not be an existing ID";
    public static final String PATROL_ID_PREFIX = "PO";
    public static final int CONSTRAINT = 0;

    public final int ID;
    public final String patrolID;

    public PatrolID (int identification) throws IllegalValueException{
        if (!isValidID(identification)){
            throw new IllegalValueException(MESSAGE_ID_CONSTRAINTS);
        }
        this.ID = identification;
        this.patrolID = PATROL_ID_PREFIX + Integer.toString(identification);
    }

    /**
     * Validates given ID.
     *
     * @throws IllegalValueException if given ID is invalid.
     */

    public static boolean isValidID(int test) {return test > CONSTRAINT; }

    @Override
    public String toString() {
        return patrolID;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PatrolID // instanceof handles nulls
                && this.patrolID.equals(((PatrolID) other).patrolID)); // state check
    }

    @Override
    public int hashCode() {
        return patrolID.hashCode();
    }

}
