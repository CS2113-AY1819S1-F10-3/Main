package seedu.addressbook.parser;

import org.junit.Before;
import org.junit.Test;
import seedu.addressbook.commands.*;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;
import static seedu.addressbook.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser();
    }

    @Test
    public void emptyInput_returnsIncorrect() {
        final String[] emptyInputs = { "", "  ", "\n  \n" };
        final String resultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, emptyInputs);
    }

    @Test
    public void unknownCommandWord_returnsHelp() {
        final String input = "unknowncommandword arguments arguments";
        parseAndAssertCommandType(input, HelpCommand.class);
    }

    /**
     * Test 0-argument commands
     */
    
    @Test
    public void helpCommand_parsedCorrectly() {
        final String input = "help";
        parseAndAssertCommandType(input, HelpCommand.class);
    }
    
    @Test
    public void clearCommand_parsedCorrectly() {
        final String input = "clear";
        parseAndAssertCommandType(input, ClearCommand.class);
    }

    @Test
    public void listCommand_parsedCorrectly() {
        final String input = "list";
        parseAndAssertCommandType(input, ListCommand.class);
    }

    @Test
    public void exitCommand_parsedCorrectly() {
        final String input = "shutdown";
        parseAndAssertCommandType(input, ExitCommand.class);
    }

    /**
     * Test single index argument commands
     */
    
    @Test
    public void deleteCommand_noArgs() {
        final String[] inputs = { "delete", "delete " };
        final String resultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, inputs);
    }

//    @Test
//    public void deleteCommand_argsIsNotSingleNumber() {
//        final String[] inputs = { "delete notAnumber ", "delete 8*wh12", "delete 1 2 3 4 5" };
//        final String resultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
//        parseAndAssertIncorrectWithMessage(resultMessage, inputs);
//    }
    
//    @Test
//    public void deleteCommand_numericArg_indexParsedCorrectly() {
//        final int testIndex = 1;
//        final String input = "delete " + testIndex;
//        final DeleteCommand result = parseAndAssertCommandType(input, DeleteCommand.class);
//        assertEquals(result.getTargetIndex(), testIndex);
//    }

    /*@Test
    public void viewCommand_noArgs() {
        final String[] inputs = { "view", "view " };
        final String resultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewAllCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, inputs);
    }

    @Test
    public void viewCommand_argsIsNotSingleNumber() {
        final String[] inputs = { "view notAnumber ", "view 8*wh12", "view 1 2 3 4 5" };
        final String resultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewAllCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, inputs);
    }
    
    @Test
    public void viewCommand_numericArg_indexParsedCorrectly() {
        final int testIndex = 2;
        final String input = "view " + testIndex;
        final ViewAllCommand result = parseAndAssertCommandType(input, ViewAllCommand.class);
        assertEquals(result.getTargetIndex(), testIndex);
    }
    */

    @Test
    public void viewAllCommand_noArgs() {
        final String[] inputs = { "viewall", "viewall " };
        final String resultMessage =
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewAllCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, inputs);
    }

    @Test
    public void viewAllCommand_argsIsNotSingleNumber() {
        final String[] inputs = { "viewall notAnumber ", "viewall 8*wh12", "viewall 1 2 3 4 5" };
        final String resultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewAllCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, inputs);
    }

    @Test
    public void viewAllCommand_numericArg_indexParsedCorrectly() {
        final int testIndex = 3;
        final String input = "viewall " + testIndex;
        final ViewAllCommand result = parseAndAssertCommandType(input, ViewAllCommand.class);
        assertEquals(result.getTargetIndex(), testIndex);
    }

    /**
     * Test find persons by keyword in name command
     */

    @Test
    public void findCommand_invalidArgs() {
        // no keywords
        final String[] inputs = {
                "find",
                "find "
        };
        final String resultMessage =
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, inputs);
    }

    @Test
    public void findCommand_validArgs_parsedCorrectly() {
        //final String[] keywords = { "key1", "key2", "key3" };
        //final Set<String> keySet = new HashSet<>(Arrays.asList(keywords));
        final String keyword = "s1234567a";
        final String input = "find " + keyword;
        final FindCommand result =
                parseAndAssertCommandType(input, FindCommand.class);
        assertEquals(keyword, result.getNric());
    }

    @Test
    public void findCommand_duplicateKeys_parsedCorrectly() {
//        final String[] keywords = { "key1", "key2", "key3" };
//        final Set<String> keySet = new HashSet<>(Arrays.asList(keywords));
        final String keyword = "s1234567a";
        // duplicate every keyword
        final String input = "find " + keyword + " " + keyword;
//        final FindCommand result =
//                parseAndAssertCommandType(input, FindCommand.class);
        final String resultMessage =
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, input);
    }

    /**
     * Test find persons by keyword in name command
     */

    @Test
    public void checkCommand_invalidArgs() {
        // no keywords
        final String[] inputs = {
                "check",
                "check "
        };
        final String resultMessage =
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CheckCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, inputs);
    }

    @Test
    public void checkCommand_validArgs_parsedCorrectly() {
        final String keyword = "s1234567a";
        final String input = "check " + keyword;
        final CheckCommand result =
                parseAndAssertCommandType(input, CheckCommand.class);
        assertEquals(keyword, result.getNricKeyword());
    }

    @Test
    public void checkCommand_invalidNricArg() {
        final String keyword = "s12345a";
        final String input = "check " + keyword;
        final String resultMessage =
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CheckCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, input);
    }

    /**
     * Test add person command
     */
    
    @Test
    public void addCommand_invalidArgs() {
        final String[] inputs = {
                "add",
                "add ",
                "add wrong args format",
                // no nric prefix
                String.format("add $s $s d/$s p/$s s/$s w/$s", Name.EXAMPLE, NRIC.EXAMPLE, DateOfBirth.EXAMPLE, PostalCode.EXAMPLE,
                        Status.EXAMPLE, Offense.EXAMPLE),
                // no dateOfBirth prefix
                String.format("add $s n/$s $s p/$s s/$s w/$s", Name.EXAMPLE, NRIC.EXAMPLE, DateOfBirth.EXAMPLE, PostalCode.EXAMPLE,
                        Status.EXAMPLE, Offense.EXAMPLE),
                // no postalCode prefix
                String.format("add $s n/$s d/$s $s s/$s w/$s", Name.EXAMPLE, NRIC.EXAMPLE, DateOfBirth.EXAMPLE, PostalCode.EXAMPLE,
                        Status.EXAMPLE, Offense.EXAMPLE),
                // no status prefix
                String.format("add $s n/$s d/$s p/$s /$s w/$s", Name.EXAMPLE, NRIC.EXAMPLE, DateOfBirth.EXAMPLE, PostalCode.EXAMPLE,
                        Status.EXAMPLE, Offense.EXAMPLE),
                // no offense(for wantedFor) prefix
                String.format("add $s n/$s d/$s p/$s s/$s /$s", Name.EXAMPLE, NRIC.EXAMPLE, DateOfBirth.EXAMPLE, PostalCode.EXAMPLE,
                        Status.EXAMPLE, Offense.EXAMPLE)
        };
        final String resultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, inputs);
    }

    @Test
    public void addCommand_invalidPersonDataInArgs() {
        final String invalidName = "[]\\[;]";
        final String validName = Name.EXAMPLE;
        final String invalidNricArg = "n/not__numbers";
        final String validNricArg = "n/" + NRIC.EXAMPLE;
        final String invalidDateOfBirthArg = "d/1000";
        final String validDateOfBirthArg = "d/" + DateOfBirth.EXAMPLE;
        final String invalidPostalCodeArg = "p/11234565";
        final String validPostalCode = "p/" + PostalCode.EXAMPLE;
        final String invalidStatusArg = "s/not a convict";
        final String validStatusArg = "s/" + Status.EXAMPLE;
        final String invalidWantedForArg = "w/no offence";
        final String validWantedForArg = "w/" + Offense.EXAMPLE;
        final String invalidTagArg = "o/invalid_-[.tag";

        // address can be any string, so no invalid address
        final String addCommandFormatString = "add $s $s $s $s $s $s";

        // test each incorrect person data field argument individually
        final String[] inputs = {
                // invalid name
                String.format(addCommandFormatString, invalidName, validNricArg, validDateOfBirthArg, validPostalCode, validStatusArg, validWantedForArg),
                // invalid nric
                String.format(addCommandFormatString, validName, invalidNricArg, validDateOfBirthArg, validPostalCode, validStatusArg, validWantedForArg),
                // invalid dateOfBirth
                String.format(addCommandFormatString, validName, validNricArg, invalidDateOfBirthArg, validPostalCode, validStatusArg, validWantedForArg),
                // invalid postalCode
                String.format(addCommandFormatString, validName, validNricArg, validDateOfBirthArg, invalidPostalCodeArg, validStatusArg, validWantedForArg),
                // invalid status
                String.format(addCommandFormatString, validName, validNricArg, validDateOfBirthArg, validPostalCode, invalidStatusArg, validWantedForArg),
                // invalid wantedFor
                String.format(addCommandFormatString, validName, validNricArg, validDateOfBirthArg, validPostalCode, validStatusArg, invalidWantedForArg),
                String.format(addCommandFormatString, validName, validNricArg, validDateOfBirthArg, validPostalCode, validStatusArg, validWantedForArg) + " " + invalidTagArg
        };
        for (String input : inputs) {
            parseAndAssertCommandType(input, IncorrectCommand.class);
        }
    }

    @Test
    public void addCommand_validPersonData_parsedCorrectly() {
        final Person testPerson = generateTestPerson();
        final String input = convertPersonToAddCommandString(testPerson);
        final AddCommand result = parseAndAssertCommandType(input, AddCommand.class);
        assertEquals(result.getPerson(), testPerson);
    }

    @Test
    public void addCommand_duplicateTags_merged() throws IllegalValueException {
        final Person testPerson = generateTestPerson();
        String input = convertPersonToAddCommandString(testPerson);
        for (Offense tag : testPerson.getPastOffenses()) {
            // create duplicates by doubling each tag
            input += " o/" + tag.getOffense();
        }

        final AddCommand result = parseAndAssertCommandType(input, AddCommand.class);
        assertEquals(result.getPerson(), testPerson);
    }

    private static Person generateTestPerson() {
        try {
            return new Person(
                new Name(Name.EXAMPLE),
                new NRIC(NRIC.EXAMPLE),
                new DateOfBirth(DateOfBirth.EXAMPLE),
                new PostalCode(PostalCode.EXAMPLE),
                new Status(Status.EXAMPLE),
                new Offense(Offense.EXAMPLE),
                new HashSet<>(Arrays.asList(new Offense("theft"), new Offense("drugs"), new Offense("riot")))
            );
        } catch (IllegalValueException ive) {
            throw new RuntimeException("test person data should be valid by definition");
        }
    }

    private static String convertPersonToAddCommandString(ReadOnlyPerson person) {
        String addCommand = "add "
                + person.getName().fullName
                + " n/" + person.getNric().getIdentificationNumber()
                + " d/" + person.getDateOfBirth().getDOB()
                + " p/" + person.getPostalCode().getPostalCode()
                + " s/" + person.getStatus().getCurrentStatus()
                + " w/" + person.getWantedFor().getOffense();
        for (Offense tag : person.getPastOffenses()) {
            addCommand += " o/" + tag.getOffense();
        }
        return addCommand;
    }

    /**
     * Utility methods
     */

    /**
     * Asserts that parsing the given inputs will return IncorrectCommand with the given feedback message.
     */
    private void parseAndAssertIncorrectWithMessage(String feedbackMessage, String... inputs) {
        for (String input : inputs) {
            final IncorrectCommand result = parseAndAssertCommandType(input, IncorrectCommand.class);
            assertEquals(result.feedbackToUser, feedbackMessage);
        }
    }

    /**
     * Utility method for parsing input and asserting the class/type of the returned command object.
     *
     * @param input to be parsed
     * @param expectedCommandClass expected class of returned command
     * @return the parsed command object
     */
    private <T extends Command> T parseAndAssertCommandType(String input, Class<T> expectedCommandClass) {
        final Command result = parser.parseCommand(input);
        assertTrue(result.getClass().isAssignableFrom(expectedCommandClass));
        return (T) result;
    }
}
