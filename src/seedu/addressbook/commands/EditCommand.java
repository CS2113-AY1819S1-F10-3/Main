package seedu.addressbook.commands;

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
            + " n/s1234567a p/510247 s/wanted w/murder o/manslaughter";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %s";
    //public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the Police Records";

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
        FindCommand findCommand = new FindCommand(nric);
        findCommand.setData(addressBook, addressBook.getAllPersons().immutableListView());
        ReadOnlyPerson toEdit = findCommand.getPersonWithNric().get(0);

        offenses.addAll(toEdit.getStringOffenses());

        DeleteCommand deleteCommand = new DeleteCommand(new NRIC(nric));

        AddCommand addCommand = new AddCommand(toEdit.getName().toString(),
                toEdit.getNRIC().getIdentificationNumber(),
                toEdit.getDateOfBirth().getDOB(),
                postalCode, status,
                wantedFor, offenses);


        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, toEdit.getNRIC().getIdentificationNumber()));
    }
}
