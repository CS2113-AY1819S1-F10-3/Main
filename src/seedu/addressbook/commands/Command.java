package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.NRIC;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList;
import seedu.addressbook.ui.Formatter;

import java.util.List;

import static seedu.addressbook.ui.Gui.DISPLAYED_INDEX_OFFSET;

/**
 * Represents an executable command.
 */
public abstract class Command {
    protected AddressBook addressBook;
    protected List<? extends ReadOnlyPerson> relevantPersons;
    private int targetIndex = -1;

    /**
     * @param targetIndex last visible listing index of the target person
     */
    public Command(int targetIndex) {
        this.setTargetIndex(targetIndex);
    }

    protected Command() {
    }

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of persons.
     *
     * @param personsDisplayed used to generate summary
     * @return summary message for persons displayed
     */
    public static String getMessageForPersonListShownSummary(List<? extends ReadOnlyPerson> personsDisplayed) {

        return "\n\n" + String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, personsDisplayed.size());
    }

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of a person.
     *
     * @param personDisplayed used to generate summary
     * @return summary message for persons displayed
     */

    public static String getMessageForPersonShownSummary(ReadOnlyPerson personDisplayed) {
        if (personDisplayed == null){
            return Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK;
        }
        else{
            String result = personDisplayed.getAsTextShowAllInVerticalMode();
            return result + "\n\n" + String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        }
    }

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of persons.
     *
     * @param timestampsDisplayed used to generate summary
     * @return summary message for timestamps displayed
     */

    public static String getMessageForScreeningHistoryShownSummary(List<String> timestampsDisplayed, String nric) {

        Formatter formatter = new Formatter();
        String result = formatter.formatForTstamps(timestampsDisplayed);

        String finalResult = result + String.format(Messages.MESSAGE_TIMESTAMPS_LISTED_OVERVIEW, nric, timestampsDisplayed.size());
        return finalResult;
    }

    /**
     * Executes the command and returns the result.
     */
    public CommandResult execute() throws IllegalValueException {
        throw new UnsupportedOperationException("This method should be implement in child classes");
    }

    //Note: it is better to make the execute() method abstract, by replacing the above method with the line below:
    //public abstract CommandResult execute();

    /**
     * Supplies the data the command will operate on.
     */
    public void setData(AddressBook addressBook, List<? extends ReadOnlyPerson> relevantPersons) {
        this.addressBook = addressBook;
        this.relevantPersons = (relevantPersons.isEmpty()) ? addressBook.getAllPersons().immutableListView() : relevantPersons;
    }

    /**
     * Extracts the the target person in the last shown list from the given arguments.
     *
     * @throws IndexOutOfBoundsException if the target index is out of bounds of the last viewed listing
     */
    protected ReadOnlyPerson getTargetPerson() throws IndexOutOfBoundsException {
        return relevantPersons.get(getTargetIndex() - DISPLAYED_INDEX_OFFSET);
    }

    //@@author andyrobert3
    protected ReadOnlyPerson getTargetPerson(NRIC nric) throws UniquePersonList.PersonNotFoundException {
        for (ReadOnlyPerson person: relevantPersons) {
            if (person.getNric().getIdentificationNumber().equals(nric.getIdentificationNumber())) {
                return person;
            }
        }
        throw new UniquePersonList.PersonNotFoundException();
    }


    public int getTargetIndex() {
        return targetIndex;
    }

    public void setTargetIndex(int targetIndex) {
        this.targetIndex = targetIndex;
    }
}
