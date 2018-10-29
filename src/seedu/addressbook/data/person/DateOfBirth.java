package seedu.addressbook.data.person;
//@@author muhdharun
import seedu.addressbook.data.exception.IllegalValueException;

import java.util.Calendar;

/**
 * Represents a Person's Date of Birth.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateOfBirth(String)}
 */

public class DateOfBirth {
    public static final String EXAMPLE = "1996";
    public static final String MESSAGE_DATE_OF_BIRTH_CONSTRAINTS = "DoB must from 1900 onwards, and less than or equal to current year";
    private static final String DATE_OF_BIRTH_VALIDATION_REGEX =  "[1-2][0-9]{3}";
    private final int year = Calendar.getInstance().get(Calendar.YEAR);
    private final String birthYear;




    /**
     * Validates given DoB.
     *
     * @throws IllegalValueException if given DoB string is invalid.
     */

    public DateOfBirth(String dob) throws IllegalValueException{
        dob = dob.trim();
        if (!isValidDateOfBirth(dob)){
            throw new IllegalValueException(MESSAGE_DATE_OF_BIRTH_CONSTRAINTS);
        }
        this.birthYear = dob;

    }


    public String getDOB() {
        return birthYear;
    }

    public boolean isValidDateOfBirth(String test) {return test.matches(DATE_OF_BIRTH_VALIDATION_REGEX) &&
            Integer.parseInt(test) <= year;}



}
