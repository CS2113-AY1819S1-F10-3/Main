package seedu.addressbook.data.exception;

public class PatrolResourceUnavailableException extends Exception {
    /**
     * @param message should contain relevant information on the failed constraint(s)
     */
    private static final String MESSAGE_UNAVAILABLE_PATROL_STATUS = "Patrol resource %s is engaged.";

    public PatrolResourceUnavailableException(String patrolId) {
        super(String.format(MESSAGE_UNAVAILABLE_PATROL_STATUS, patrolId));
    }
}
