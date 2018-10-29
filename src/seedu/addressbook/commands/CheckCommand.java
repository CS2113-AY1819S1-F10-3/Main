package seedu.addressbook.commands;

//@@author muhdharun
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns the list of timestamps for when a perosn was screened using 'find' command, if person exists in PRISM
 */

public class CheckCommand extends Command {

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

    /**
     *
     * @param nric
     * @return list of timestamps converted to strings
     */

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
