package seedu.addressbook.data.person;

import seedu.addressbook.data.exception.IllegalValueException;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Person's identification number(NRIC or FIN) in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNRIC(String)}
 */

public class NRIC {
    public static final String EXAMPLE = "s1234567a";
    public static final String MESSAGE_NAME_CONSTRAINTS = "NRIC/FIN should start with 's'/'t'/'g'/'f' and end with a letter and " +
            "must have 7 digits in between, no spaces";
    public static final String NAME_VALIDATION_REGEX = "[sStTgGfF][0-9]{7}[a-zA-Z]";

    public final String identificationNumber;

    /**
     * Validates given nric.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public NRIC(String nric) throws IllegalValueException {
        nric = nric.trim();
        if (!isValidNRIC(nric)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.identificationNumber = nric;
    }

    /**
     * Returns true if a given string is a valid NRIC.
     */
    public static boolean isValidNRIC(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return identificationNumber;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NRIC // instanceof handles nulls
                && this.identificationNumber.equals(((NRIC) other).identificationNumber)); // state check
    }

    @Override
    public int hashCode() {
        return identificationNumber.hashCode();
    }
}
