package seedu.addressbook.commands;

import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.NRIC;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.ReadOnlyPerson;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Finds person with specified NRIC \n\t"
            + "Parameters: NRIC ...\n\t"
            + "Example: " + COMMAND_WORD + " s1234567a";

    //private final Set<String> nricToFind;
    private String nricKeyword;

    public FindCommand(String nricToFind)
    {
        this.nricKeyword = nricToFind;
    }

//    /**
//     * Returns copy of the nric in this command.
//     */
//    public Set<NRIC> getNricToFind() {
//        return new HashSet<>(nricToFind);
//    }

    public String getNricKeyword(){
        return nricKeyword;
    }

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
    private List<ReadOnlyPerson> getPersonsWithNric(String nric){
        final List<ReadOnlyPerson> matchedPerson = new ArrayList<>();
        for (ReadOnlyPerson person : addressBook.getAllPersonsDirect()) {
            if (person.getNRIC().getIdentificationNumber().equals(nric)) {
                matchedPerson.add(person);
                addressBook.addPersontoDbAndUpdate(person);
                try {
                    addressBook.updateDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return matchedPerson;
    }

}
