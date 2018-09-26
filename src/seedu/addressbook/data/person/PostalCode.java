package seedu.addressbook.data.person;

import seedu.addressbook.data.exception.IllegalValueException;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Person's Postal Code in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPostalCode(String)}
 */

public class PostalCode {

    public static final String EXAMPLE = "510123";
    public static final String MESSAGE_NAME_CONSTRAINTS = "Must be 6 digits long";
    public static final String NAME_VALIDATION_REGEX = "[0-9]{6}";

    public final String postalCode;

    /**
     * Validates given Postal Code.
     *
     * @throws IllegalValueException if given Postal Code string is invalid.
     */
    public PostalCode(String pc) throws IllegalValueException {
        pc = pc.trim();
        if (!isValidPostalCode(pc)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.postalCode = pc;
    }

    /**
     * Returns true if a given string is a valid NRIC.
     */
    public static boolean isValidPostalCode(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return postalCode;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PostalCode // instanceof handles nulls
                && this.postalCode.equals(((PostalCode) other).postalCode)); // state check
    }

    @Override
    public int hashCode() {
        return postalCode.hashCode();
    }

}
