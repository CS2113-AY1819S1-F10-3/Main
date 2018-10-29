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
    private NRIC nric;
    private PostalCode postalCode;
    private Status status;

    public NRIC getNric() {
        return nric;
    }

    public PostalCode getPostalCode() {
        return postalCode;
    }

    public Status getStatus() {
        return status;
    }

    public Offense getWantedFor() {
        return wantedFor;
    }

    private Offense wantedFor;
    private Set<Offense> offenses;

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Edits the person identified by the NRIC number.\n\t"
            + "Contact details can be marked private by prepending 'p' to the prefix.\n\t"
            + "Parameters: n/NRIC p/POSTALCODE s/STATUS w/WANTEDFOR [o/PASTOFFENSES]...\n\t"
            + "Example: " + COMMAND_WORD
            + " n/s1234567a p/510247 s/wanted w/murder o/gun";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %s";

    private void updatePerson() throws UniquePersonList.PersonNotFoundException {
        for (Person person : addressBook.getAllPersons()) {
            if (person.getNric().equals(this.nric)) {
                person.setPostalCode(postalCode);
                person.setWantedFor(wantedFor);
                person.setStatus(status);
                person.addPastOffenses(offenses);

                return;
            }
        }

        throw new UniquePersonList.PersonNotFoundException();
    }

    public EditCommand(String nric,
                       String postalCode,
                       String status,
                       String wantedFor,
                       Set<String> offenses) throws IllegalValueException {

        this.nric = new NRIC(nric);
        this.postalCode = new PostalCode(postalCode);
        this.status = new Status(status);
        this.wantedFor = new Offense(wantedFor);
        this.offenses = Offense.getOffenseSet(offenses);
    }

    @Override
    public CommandResult execute() {
        try {
            this.updatePerson();
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, this.nric));
        } catch(UniquePersonList.PersonNotFoundException pnfe) {
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        }

    }
}
