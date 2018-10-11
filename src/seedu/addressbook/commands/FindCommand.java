package seedu.addressbook.commands;

import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.NRIC;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.ReadOnlyPerson;

import java.sql.Timestamp;
import java.util.*;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Finds person with specified NRIC \n\t"
            + "Parameters: NRIC ...\n\t"
            + "Example: " + COMMAND_WORD + " S1234567A";

    private final Set<String> nricToFind;
    private String nricKeyword;

    public FindCommand(Set<String> nricToFind)
    {
        for (String nric:nricToFind){
            this.nricKeyword = nric;
        }
        this.nricToFind = nricToFind;
    }

//    /**
//     * Returns copy of the nric in this command.
//     */
//    public Set<NRIC> getNricToFind() {
//        return new HashSet<>(nricToFind);
//    }

    @Override
    public CommandResult execute() {
        final List<ReadOnlyPerson> personsFound = getPersonsWithNric(nricKeyword);
        return new CommandResult(getMessageForPersonListShownSummary(personsFound), personsFound);
    }

    /**
     * Retrieve all persons in the address book whose names contain some of the specified keywords.
     *
     * @param nric for searching
     * @return list of persons found
     */
    private List<ReadOnlyPerson> getPersonsWithNric(String nric) {
        final List<ReadOnlyPerson> matchedPerson = new ArrayList<>();
        for (ReadOnlyPerson person : addressBook.getAllPersons()) {
            final Set<String> wordsInName = new HashSet<>(person.getName().getWordsInName());
            if (person.getNRIC().getIdentificationNumber().equals(nricKeyword)) {
                matchedPerson.add(person);
                person.getScreeningHistory().add(new Timestamp(System.currentTimeMillis()));
            }
        }
        return matchedPerson;
    }

}
