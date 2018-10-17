package seedu.addressbook.commands;

import seedu.addressbook.data.person.ReadOnlyPerson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

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
        final List<ReadOnlyPerson> personFound = getPersonWithNric();
        return new CommandResult(getMessageForPersonListShownSummary(personFound), personFound);
    }


    // TODO: Make this return only one ReadOnlyPerson
    /**
     * Retrieve all persons in the address book whose names contain some of the specified keywords.
     *
     * @return Persons found, null if no person found
     */
    public List<ReadOnlyPerson> getPersonWithNric() {
        List<ReadOnlyPerson> matchedPerson = new ArrayList<>();
        for (ReadOnlyPerson person : relevantPersons) {
            if (person.getNric().getIdentificationNumber().equals(nric)) {
                addressBook.addPersontoDbAndUpdate(person);
                try {
                    addressBook.updateDatabase();
                } catch (IOException e) {
                    e.printStackTrace(); // TODO: throws exeception
                }
                matchedPerson.add(person);

                return matchedPerson;
            }
        }

        return null;
    }

}
