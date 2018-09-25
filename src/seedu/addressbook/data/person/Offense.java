package seedu.addressbook.data.person;

import seedu.addressbook.data.exception.IllegalValueException;
import java.util.Arrays;

public class Offense {
    public static final String EXAMPLE = "theft";
    public static final String MESSAGE_OFFENSE_CONSTRAINTS = "Offense should be in lower case and must be inside the list";

    private static final String[] OFFENSE_LIST = {"theft","drugs","riot"};

    public final String offense;

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

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidOffense(String test) {
        return Arrays.asList(OFFENSE_LIST).contains(test);
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
