package seedu.addressbook.data.exception;

/**
 * TODO: Add Javadoc comment
 * Signals that a PO is not free for dispatch
 */
public class PatrolResourceUnavailableException extends Exception {
    /**
     * @param message should contain relevant information on the failed constraint(s)
     */
    private static final String MESSAGE_UNAVAILABLE_PATROL_STATUS = "Patrol resource %s is engaged.";

    public PatrolResourceUnavailableException(String patrolId) {
        super(String.format(MESSAGE_UNAVAILABLE_PATROL_STATUS, patrolId));
    }
}
