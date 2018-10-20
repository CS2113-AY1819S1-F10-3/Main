package seedu.addressbook.commands;


import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.storage.jaxb.AdaptedAddressBook;
import seedu.addressbook.storage.jaxb.AdaptedPerson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckCommand extends Command {
    //TODO: make test cases for:
    //Check with invalid NRIC
    //Check with no IC
    //Check with non-existing NRIC

    public static final String COMMAND_WORD = "check";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Gets screening history of person with specified NRIC \n\t"
            + "Parameters: NRIC ...\n\t"
            + "Example: " + COMMAND_WORD + " s1234567a";
    private String nricKeyword;


    public CheckCommand(String nricToFind)
    {
        this.nricKeyword = nricToFind;
    }

    public String getNricKeyword(){
        return nricKeyword;
    }



    @Override
    public CommandResult execute() {
        final List<String> screeningHistory = getPersonWithNric(nricKeyword);
        return new CommandResult(getMessageForScreeningHistoryShownSummary(screeningHistory,nricKeyword));
    }

    private List<String> getPersonWithNric(String nric){
        List<String> screeningHistory = new ArrayList<>();
        try {
            screeningHistory = addressBook.readDatabase(nric);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screeningHistory;
    }

}
