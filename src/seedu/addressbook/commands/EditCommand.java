//@@author andyrobert3
package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;

import java.util.Set;


/**
 * Edits existing person in police records.
 */
public class EditCommand extends Command {
    private String nric;
    private String postalCode;
    private String status;
    private String wantedFor;
    private Set<String> offenses;

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Edits the person identified by the NRIC number.\n\t"
            + "Contact details can be marked private by prepending 'p' to the prefix.\n\t"
            + "Parameters: n/NRIC p/POSTALCODE s/STATUS w/WANTEDFOR [o/PASTOFFENSES]...\n\t"
            + "Example: " + COMMAND_WORD
            + " n/s1234567a p/510247 s/wanted w/murder o/gun";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %s";
    //public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the Police Records";

    private void updatePerson() throws IllegalValueException, UniquePersonList.PersonNotFoundException {
        for (Person person : addressBook.getAllPersons()) {
            if (person.getNric().getIdentificationNumber().equals(this.nric)) {
                person.setPostalCode(new PostalCode(postalCode));
                person.setWantedFor(new Offense(wantedFor));
                person.setStatus(new Status(status));
                person.addPastOffenses(Offense.getOffenseSet(offenses));

                return;
            }
        }

        throw new UniquePersonList.PersonNotFoundException();
    }

    public EditCommand(String nric,
                       String postalCode,
                       String status,
                       String wantedFor,
                       Set<String> offenses) {

        this.nric = nric;
        this.postalCode = postalCode;
        this.status = status;
        this.wantedFor = wantedFor;
        this.offenses = offenses;
    }

    @Override
    public CommandResult execute() throws IllegalValueException {
        try {
            this.updatePerson();
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, this.nric));
        } catch(UniquePersonList.PersonNotFoundException pnfe) {
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        }

    }
}
