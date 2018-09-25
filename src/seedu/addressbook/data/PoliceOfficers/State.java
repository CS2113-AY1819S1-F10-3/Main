package seedu.addressbook.data.PoliceOfficers;

import seedu.addressbook.data.exception.IllegalValueException;

/**
 * Represents a Patrol resource's current state in EX-SI-53.
 * Guarantees: only 2 possible states; is valid as declared in {@link #isValidState(String)}
 */

public class State {

    public static final String EXAMPLE_STATE = "free";
    public static final String MESSAGE_STATE_CONSTRAINTS = "Patrol resource can either be 'free' or 'engaged'";
    public static final String FREE = "free";
    public static final String ENGAGED = "engaged";
    public static final String DEFAULT_CASE = "none";
    public String currentState;


    public State(String state) throws IllegalValueException{
        state = state.trim();
        if (!isValidState(state)){
            throw new IllegalValueException(MESSAGE_STATE_CONSTRAINTS);
        }
        this.currentState = state;
    }

    public boolean isValidState(String test) {return test == FREE || test == ENGAGED;}

    @Override
    public String toString() {
        return currentState;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PatrolID // instanceof handles nulls
                && this.currentState.equals(((State) other).currentState)); // state check
    }

    @Override
    public int hashCode() {
        return currentState.hashCode();
    }

}
