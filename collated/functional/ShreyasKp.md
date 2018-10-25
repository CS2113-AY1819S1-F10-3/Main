# ShreyasKp
###### \seedu\addressbook\autocorrect\CheckDistance.java
``` java
package seedu.addressbook.autocorrect;

import seedu.addressbook.commands.Dictionary;

import java.util.ArrayList;

/**
 * Checks the number of single character changes required to convert one string to another.
 */
public class CheckDistance {

    Dictionary dictionary = new Dictionary();
    EditDistance calculate = new EditDistance();

    ArrayList<String> commandList = dictionary.getCommands();

    ArrayList<String> detailsList = dictionary.getDetails();

    String prediction = "none";

    public String checkDistance(String commandInput) {
        int distance, check = 0;
        for (String command : commandList) {
            distance = calculate.computeDistance(commandInput, command);
            if (distance == 1) {
                prediction = command;
                check = 1;
                break;
            }
        }
        if (check == 0) {
            prediction = "none";
        }
        return prediction;
    }

    public String checkInputDistance(String input) {
        int distance, check = 0;
        ArrayList<String> detailsList = dictionary.getDetails();
        for (String nric : detailsList) {
            distance = calculate.computeDistance(input, nric);
            if (distance == 1 || distance == 2) {
                prediction = nric;
                check = 1;
                break;
            }
        }
        if (check == 0) {
            prediction = "none";
        }
        return prediction;
    }

    public Integer checkCommandDistance(String commandInput) {
        int distance, check = 0;
        for (String command : commandList) {
            distance = calculate.computeDistance(commandInput, command);
            if (distance == 0) {
                check = 1;
                break;
            }
        }
        return check;
    }
}
```
###### \seedu\addressbook\autocorrect\EditDistance.java
``` java
package seedu.addressbook.autocorrect;

/**
 * Returns the edit distance needed to convert one string to the other.
 */
public class EditDistance {

    /**
     * If returns 0, the strings are same.
     * If returns 1, that means either a character is added, removed or replaced and so on.
     *
     * @param inputString The string input by the user.
     * @param storedString The string contained in the Dictionary.
     * @return The minimum number of operations required to transform inputString into storedString.
     */
    public static int computeDistance(String inputString, String storedString) {

        int length1 = inputString.length();
        int length2 = storedString.length();

        //Solution below adapted from https://www.programcreek.com/2013/12/edit-distance-in-java/
        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[length1 + 1][length2 + 1];

        for (int i = 0; i <= length1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= length2; j++) {
            dp[0][j] = j;
        }

        //iterate through, and check last char
        for (int i = 0; i < length1; i++) {
            char c1 = inputString.charAt(i);
            for (int j = 0; j < length2; j++) {
                char c2 = storedString.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[length1][length2];
    }
}
```
###### \seedu\addressbook\commands\DateTimeCommand.java
``` java
package seedu.addressbook.commands;
import seedu.addressbook.timeanddate.TimeAndDate;

public class DateTimeCommand extends Command {

    public static final String COMMAND_WORD = "time";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Returns current date and time \n\t"
            + "Example: " + COMMAND_WORD;

    public TimeAndDate timeAndDate;

    public DateTimeCommand(){
        timeAndDate = new TimeAndDate();
    }

    @Override
    public CommandResult execute() {
        return new CommandResult(timeAndDate.outputDATHrs());
    }
}
```
###### \seedu\addressbook\commands\DeleteCommand.java
``` java
            CheckDistance checker = new CheckDistance();

            String nric = toDelete.toString();
            String prediction = checker.checkInputDistance(nric);

            if(!prediction.equals("none")) {
                return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK
                        + "\n"
                        + "Did you mean to use "
                        + prediction);
            } else {
                return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK );
            }
        }
    }

}
```
###### \seedu\addressbook\commands\Dictionary.java
``` java
package seedu.addressbook.commands;

import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.storage.StorageFile;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stores all the commands to be compared with for autocorrect function.
 */
public class Dictionary extends Command{

    public static final ArrayList<String> commands = new ArrayList<>();

    public String errorMessage = "Did you mean to use %s?" + "\n" + "Please try using the correct implementation of the input as shown below-";

    public String errorCommandMessage = "Did you mean to use %s?" + "\n" + "Please try using the correct implementation of the command as shown below-";

    public ArrayList<String> details = new ArrayList<>();
    private AddressBook addressBook;

    public Dictionary() {
        try {
            StorageFile storage = new StorageFile();
            this.addressBook = storage.load();
        } catch(Exception e) {

        } finally {
            commands.add(AddCommand.COMMAND_WORD);
            commands.add(CheckCommand.COMMAND_WORD);
            commands.add(ClearCommand.COMMAND_WORD);
            commands.add(DateTimeCommand.COMMAND_WORD);
            commands.add(DeleteCommand.COMMAND_WORD);
            commands.add(EditCommand.COMMAND_WORD);
            commands.add(ExitCommand.COMMAND_WORD);
            commands.add(FindCommand.COMMAND_WORD);
            commands.add(HelpCommand.COMMAND_WORD);
            commands.add(InboxCommand.COMMAND_WORD);
            commands.add(ReadCommand.COMMAND_WORD);
            commands.add(ListCommand.COMMAND_WORD);
            commands.add(LockCommand.COMMAND_WORD);
            commands.add(RequestHelp.COMMAND_WORD);
            commands.add(DispatchBackup.COMMAND_WORD);
            commands.add(CheckPOStatusCommand.COMMAND_WORD);
            commands.add(UpdateStatusCommand.COMMAND_WORD);
            //commands.add(UpdatePasswordCommand.COMMAND_WORD);
            commands.add(ViewAllCommand.COMMAND_WORD);
        }


    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getCommandErrorMessage() {
        return errorCommandMessage;
    }

    public static ArrayList<String> getCommands() {
        return commands;
    }

    public ArrayList<String> getDetails() {
        try {


            for (ReadOnlyPerson person : addressBook.getAllPersons().immutableListView()) {
                details.add(person.getNric().getIdentificationNumber());
            }
            return details;
        }
        catch(NullPointerException e) {
            ArrayList<String> x = new ArrayList<>();
            x.addAll(new ArrayList<>(Arrays.asList("Empty")));
            return x;
        }
    }
}
```
###### \seedu\addressbook\ui\MainWindow.java
``` java
        else {
            userCommandText = userCommandText.trim();
            CheckDistance checker = new CheckDistance();
            Dictionary dict = new Dictionary();
            String arr[] = userCommandText.split(" ", 2);
            String commandWordInput = arr[0];
            if((checker.checkCommandDistance(commandWordInput)).equals(0)) {
                String output = checker.checkDistance(commandWordInput);
                String displayCommand = "not working";
                if(!(output.equals("none"))) {
                    clearScreen();
                    display(String.format(dict.getCommandErrorMessage(), output));
                    switch (output) {
                        case AddCommand.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case DateTimeCommand.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DateTimeCommand.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case DeleteCommand.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case EditCommand.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case ClearCommand.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case FindCommand.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case ListCommand.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case ViewAllCommand.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewAllCommand.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case ExitCommand.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExitCommand.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case LockCommand.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LockCommand.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case DispatchBackup.COMMAND_WORD:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DispatchBackup.MESSAGE_USAGE)).feedbackToUser;
                            break;

                        case HelpCommand.COMMAND_WORD: // Fallthrough
                        default:
                            displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE)).feedbackToUser;
                    }
                    int i = displayCommand.indexOf("!");
                    display(displayCommand.substring(i + 1));
                }
                else {
                    boolean isHQPFlag = password.isHQPUser();
                    if(isHQPFlag) {
                        displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_ALL_USAGES)).feedbackToUser;
                    }
                    else {
                        displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_PO_USAGES)).feedbackToUser;
                    }
                    clearScreen();
                    display(displayCommand);
                }
            }
            else{
                CommandResult result = logic.execute(userCommandText);
                displayResult(result);
                clearCommandInput();
            }
        }
    }

```
