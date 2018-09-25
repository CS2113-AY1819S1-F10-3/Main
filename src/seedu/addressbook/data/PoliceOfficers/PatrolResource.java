package seedu.addressbook.data.PoliceOfficers;


/**
 * Represents a Patrol Resource in the system.
 * Guarantees: details are present and not null, field values are validated.
 */
public class PatrolResource {
    public PatrolID patrolID;
    public State state;
    public Case currentCase;


    /**
     * Assumption: Every field must be present and not null (except current case).
     */
    public PatrolResource(PatrolID patrolID, State state, Case currentCase) {
        this.patrolID = patrolID;
        this.state = state;
        this.currentCase = (state.currentState == state.FREE) ? new Case() : currentCase;
    }

    public PatrolResource(ReadOnlyPatrolResource source) {
        this(source.getPatrolID(), source.getState(), source.getCurrentCase());
    }

    public Case RA(){
        //would generate timestamp and GPS
        //sent to HQP 'inbox'
    }

    public Case RB(){
        //would generate timestamp and GPS
        //sent to HQP 'inbox'
    }

    public Case RF(){
        //would generate timestamp and GPS
        //sent to HQP 'inbox'
    }

    @Override
    public PatrolID getPatrolID() {
        return patrolID;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public Case getCurrentCase() {
        return currentCase;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyPatrolResource // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyPatrolResource) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(patrolID, state, currentCase);
    }

    @Override
    public String toString() {
        return getAsTextShowAll();
    }

}
