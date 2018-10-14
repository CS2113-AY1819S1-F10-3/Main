package seedu.addressbook.data.person;

import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.inbox.Msg;

import java.util.HashMap;

public class Offense {
    public static final String EXAMPLE = "theft";
    public static final String MESSAGE_OFFENSE_CONSTRAINTS = "Offense should be in lower case and must be inside the list";
    public static final String MESSAGE_OFFENSE_INVALID = "Offense entered is not in database. \n" +
            "                                                   Please request backup again with different offense.";

    private static HashMap<String, Msg.Priority> OFFENSE_LIST = new HashMap<>();
    static {
        OFFENSE_LIST.put("theft", Msg.Priority.HIGH);
        OFFENSE_LIST.put("drugs", Msg.Priority.LOW);
        OFFENSE_LIST.put("riot", Msg.Priority.HIGH);
        OFFENSE_LIST.put("murder", Msg.Priority.MED);
        OFFENSE_LIST.put("fleeing suspect", Msg.Priority.MED);
        OFFENSE_LIST.put("gun", Msg.Priority.HIGH);
    }

    public static final String NULL_OFFENSE = "none";
    private final String offense;

    public Offense(){
        this.offense = "none";
    }

    /**
     * Validates given tag name.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Offense(String name) throws IllegalValueException {
        name = name.trim();
        if (!isValidOffense(name)) {
            throw new IllegalValueException(MESSAGE_OFFENSE_CONSTRAINTS);
        }
        this.offense = name;
    }

    public String getOffense() {
        return offense;
    }

    /**
     * Returns priority for a given offense.
     *
     * @params offense name
     * @return priority
     */
    public static Msg.Priority getPriority(String offense) throws IllegalValueException {
        if (!OFFENSE_LIST.containsKey(offense)) {
            throw new IllegalValueException("Offense does not exist in database.");
        }

        return OFFENSE_LIST.get(offense);
    }

    public static int toInteger(Msg.Priority priority) {
        return priority.getPriority();
    }


    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidOffense(String offense) {
        return OFFENSE_LIST.containsKey(offense);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Offense // instanceof handles nulls
                && this.offense.equals(((Offense) other).offense)); // state check
    }

    @Override
    public int hashCode() {
        return offense.hashCode();
    }

    @Override
    public String toString() {
        return '[' + offense + ']';
    }
}
