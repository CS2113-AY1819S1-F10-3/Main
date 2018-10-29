package seedu.addressbook.commands;

import seedu.addressbook.data.person.ReadOnlyPerson;

import java.io.IOException;


/**
 * Finds a particular person with the specified NRIC, used for screening.
 * Letters in NRIC must be in lower case.
 */
public class FindCommand extends Command {
    //@@author muhdharun -reused
    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Finds person with specified NRIC \n\t"
            + "Parameters: NRIC ...\n\t"
            + "Example: " + COMMAND_WORD + " s1234567a";

    private String nric;

    public FindCommand(String nricToFind) {
        this.nric = nricToFind;
    }

    public String getNric(){
        return nric;
    }

    @Override
    public CommandResult execute() {
        try {
            final ReadOnlyPerson personFound = getPersonWithNric();
            return new CommandResult(getMessageForPersonShownSummary(personFound));
        } catch(IOException ioe) {
            return new CommandResult("Cannot find person with nric");
        }
    }


    /**
     * Retrieve the person in PRISM whose name contain the specified NRIC.
     *
     * @return Persons found, null if no person found
     */
    public ReadOnlyPerson getPersonWithNric() throws IOException {
        for (ReadOnlyPerson person : relevantPersons) {
            if (person.getNric().getIdentificationNumber().equals(nric)) {
                addressBook.addPersonToDbAndUpdate(person);
                addressBook.updateDatabase();
                return person;
            }
        }

        return null;
    }

}
