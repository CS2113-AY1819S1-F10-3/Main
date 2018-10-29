package seedu.addressbook.logic;


import org.javatuples.Triplet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import seedu.addressbook.PatrolResourceStatus;
import seedu.addressbook.commands.*;
import seedu.addressbook.common.Location;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.person.*;
import seedu.addressbook.inbox.*;
import seedu.addressbook.password.Password;
import seedu.addressbook.storage.StorageFile;
import seedu.addressbook.timeanddate.TimeAndDate;
import seedu.addressbook.ui.Formatter;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static seedu.addressbook.common.Messages.*;
import static seedu.addressbook.password.Password.*;


public class LogicTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private StorageFile saveFile;
    private AddressBook addressBook;
    private Logic logic;

    @Before
    public void setup() throws Exception {
        saveFile = new StorageFile(saveFolder.newFile("testSaveFile.txt").getPath());
        addressBook = new AddressBook();
        saveFile.save(addressBook);
        logic = new Logic(saveFile, addressBook);
    }

    @Test
    public void constructor() {
        //Constructor is called in the setup() method which executes before every test, no need to call it here again.

        //Confirm the last shown list is empty
        assertEquals(Collections.emptyList(), logic.getLastShownList());
    }

    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'address book' and the 'last shown list' are expected to be empty.
     * @see #assertCommandBehavior(String, String, AddressBook, boolean, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, AddressBook.empty(),false, Collections.emptyList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the Logic object's state are as expected:<br>
     *      - the internal address book data are same as those in the {@code expectedAddressBook} <br>
     *      - the internal 'last shown list' matches the {@code expectedLastList} <br>
     *      - the storage file content matches data in {@code expectedAddressBook} <br>
     */
    private void assertCommandBehavior(String inputCommand,
                                      String expectedMessage,
                                      AddressBook expectedAddressBook,
                                      boolean isRelevantPersonsExpected,
                                      List<? extends ReadOnlyPerson> lastShownList) throws Exception {

        //Execute the command
        CommandResult r = logic.execute(inputCommand);

        //Confirm the result contains the right data
        assertEquals(expectedMessage, r.feedbackToUser);
        assertEquals(r.getRelevantPersons().isPresent(), isRelevantPersonsExpected);
        if(isRelevantPersonsExpected){
            assertEquals(lastShownList, r.getRelevantPersons().get());
        }

        //Confirm the state of data is as expected
        assertEquals(expectedAddressBook, addressBook);
        assertEquals(lastShownList, logic.getLastShownList());
        assertEquals(addressBook, saveFile.load());
    }

    //@@author ongweekeong

    /**
     * Executes the command and confirms that the result message (Timestamp) is correct (within the given tolerance threshold)
     * @param inputCommand
     * @param expectedMessage
     * @param tolerance
     * @throws Exception
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage, int tolerance) throws Exception {
        CommandResult r = logic.execute(inputCommand);
        String[] parts = r.feedbackToUser.split("-", 2);
        String[] expected = expectedMessage.split("-", 2);
        assertEqualsTimestamp(expected[1], parts[1], tolerance);

        if(parts[0].equals(expected[0])){
            expectedMessage = r.feedbackToUser;
        }
        assertEquals(expectedMessage, r.feedbackToUser);
    }

    private void assertEqualsTimestamp(String time1, String time2, int tolerance){
        String[] inputTime = time1.split(":", 4);
        String[] expectedTime = time2.split(":", 4);
        inputTime[3] = inputTime[2] + inputTime[3].substring(0, 3);
        expectedTime[3] = expectedTime[2] + expectedTime[3].substring(0, 3);
        int difference = abs(Integer.parseInt(inputTime[3]) - Integer.parseInt(expectedTime[3]));
        if(inputTime[0].equals(expectedTime[0]) && inputTime[1].equals(expectedTime[1]) && inputTime[2].equals(expectedTime[2]) &&
                (difference <= tolerance)){
            time2 = time1;
        }
        assertEquals(time1, time2);
    }

    private String assertCommandBehavior(String inputCommand, String expectedResult, Msg testMsg, int msgIndex) throws Exception {
        CommandResult r = logic.execute(inputCommand);
        String inputTime = parseMsgForTimestamp(r.feedbackToUser);
        String expectedTime = parseMsgForTimestamp(testMsg.getTimeString());

        assertEqualsTimestamp(inputTime, expectedTime, 300);

        testMsg.setTime(adjustExpectedTimestamp(r.feedbackToUser, msgIndex));

        expectedResult += InboxCommand.concatenateMsg(msgIndex, testMsg);

        assertEquals(String.format(expectedResult, msgIndex), r.feedbackToUser);
        return expectedResult;
    }

    //@@author iamputradanish
    @Test
    public void execute_unknownCommandWord_forHQP() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        Password.unlockHQP();
        assertCommandBehavior(unknownCommand, HelpCommand.MESSAGE_ALL_USAGES);
        Password.lockIsHQP();
    }

    @Test
    public void execute_help_forHQP() throws Exception {
        Password.unlockHQP();
        assertCommandBehavior("help", HelpCommand.MESSAGE_ALL_USAGES);
        Password.lockIsHQP();
    }

    //@@author ShreyasKp
    @Test
    public void execute_timeCommand() throws Exception {
        String command = DateTimeCommand.COMMAND_WORD;
        TimeAndDate timeAndDate = new TimeAndDate();
        assertCommandBehavior(command, timeAndDate.outputDATHrs(), 200);
    }

    //@@author iamputradanish

    @Test
    public void execute_unknownCommandWord_forPO() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        unlockPO();
        assertCommandBehavior(unknownCommand, HelpCommand.MESSAGE_PO_USAGES);
        Password.lockIsPO();
    }

    @Test
    public void execute_help_forPO() throws Exception {
        unlockPO();
        assertCommandBehavior("help", HelpCommand.MESSAGE_PO_USAGES);
        Password.lockIsPO();
    }

    //@@author
    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("shutdown", ExitCommand.MESSAGE_EXIT_ACKNOWEDGEMENT);
    }

    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        addressBook.addPerson(helper.generatePerson(1));
        addressBook.addPerson(helper.generatePerson(2));
        addressBook.addPerson(helper.generatePerson(3));

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, AddressBook.empty(), false, Collections.emptyList());
    }
//@@author muhdharun -reused
    @Test
    public void execute_add_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertCommandBehavior(
                "add wrong args wrong args wrong args", expectedMessage);
        assertCommandBehavior(
                "add Valid Name s1234567a d/1980 p/123456 s/clear w/none", expectedMessage);
        assertCommandBehavior(
                "add Valid Name n/s1234567a 1980 p/123456 s/clear w/none", expectedMessage);
        assertCommandBehavior(
                "add Valid Name n/s1234567a d/1980 123456 s/clear w/none", expectedMessage);
        assertCommandBehavior(
                "add Valid Name n/s1234567a d/1980 p/123456 clear w/none", expectedMessage);
        assertCommandBehavior(
                "add Valid Name n/s1234567a d/1980 123456 s/clear none", expectedMessage);
    }

    @Test
    public void execute_add_invalidPersonData() throws Exception {
        assertCommandBehavior(
                "add []\\[;] n/s1234567a d/1980 p/123456 s/clear w/none", Name.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "add Valid Name n/s123457a d/1980 p/123456 s/clear w/none", NRIC.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "add Valid Name n/s1234567a d/188 p/123456 s/clear w/none", DateOfBirth.MESSAGE_DATE_OF_BIRTH_CONSTRAINTS);
        assertCommandBehavior(
                "add Valid Name n/s1234567a d/1980 p/13456 s/clear w/none", PostalCode.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "add Valid Name n/s1234567a d/1980 p/123456 s/xc w/none o/rob", Offense.MESSAGE_OFFENSE_INVALID + "\n" + Offense.getListOfValidOffences());
        assertCommandBehavior(
                "add Valid Name n/s1234567a d/1980 p/123456 s/wanted w/none o/none", Person.WANTED_FOR_WARNING);

    }
//@@author
    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Person toBeAdded = helper.adam();
        AddressBook expectedAB = new AddressBook();
        expectedAB.addPerson(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                              String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                              expectedAB,
                              false,
                              Collections.emptyList());

    }

    @Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Person toBeAdded = helper.adam();
        AddressBook expectedAB = new AddressBook();
        expectedAB.addPerson(toBeAdded);

        // setup starting state
        addressBook.addPerson(toBeAdded); // person already in internal address book

        // execute command and verify result
        assertCommandBehavior(
                helper.generateAddCommand(toBeAdded),
                AddCommand.MESSAGE_DUPLICATE_PERSON,
                expectedAB,
                false,
                Collections.emptyList());

    }

    @Test
    public void execute_list_showsAllPersons() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        AddressBook expectedAB = helper.generateAddressBook(false, false);
        List<? extends ReadOnlyPerson> expectedList = expectedAB.getAllPersons().immutableListView();

        // prepare address book state

        helper.addToAddressBook(addressBook, false, false);
        assertCommandBehavior("list",
                              Command.getMessageForPersonListShownSummary(expectedList),
                              expectedAB,
                              true,
                              expectedList);
    }


    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single person in the last shown list, using visible index.
     * @param commandWord to test assuming it targets a single person in the last shown list based on visible index.
     */
    private void assertInvalidIndexBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Person> lastShownList = helper.generatePersonList(false, true);

        logic.setLastShownList(lastShownList);

        assertCommandBehavior(commandWord + " -1", expectedMessage, AddressBook.empty(), false, lastShownList);
        assertCommandBehavior(commandWord + " 0", expectedMessage, AddressBook.empty(), false, lastShownList);
        assertCommandBehavior(commandWord + " 3", expectedMessage, AddressBook.empty(), false, lastShownList);

    }

    private void assertInvalidCommandFormatBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = NRIC.MESSAGE_NAME_CONSTRAINTS;
        TestDataHelper helper = new TestDataHelper();
        List<Person> lastShownList = helper.generatePersonList(false, true);

        logic.setLastShownList(lastShownList);

        assertCommandBehavior(commandWord + " -1", expectedMessage, AddressBook.empty(), false, lastShownList);
        assertCommandBehavior(commandWord + " 0", expectedMessage, AddressBook.empty(), false, lastShownList);
        assertCommandBehavior(commandWord + " 3", expectedMessage, AddressBook.empty(), false, lastShownList);

    }



    @Test
    public void execute_request_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RequestHelpCommand.MESSAGE_USAGE);
        assertCommandBehavior("rb", expectedMessage);
        assertCommandBehavior("rb    ", expectedMessage);
    }

    //@@author andyrobert3
    @Test
    public void execute_request_invalidOffense() throws Exception {
        String expectedMessage =  Offense.MESSAGE_OFFENSE_INVALID;
        assertCommandBehavior("rb crime", expectedMessage);
        assertCommandBehavior("rb tired", expectedMessage);
    }

    //@@author
    @Test
    public void execute_viewAll_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewAllCommand.MESSAGE_USAGE);
        assertCommandBehavior("viewall ", expectedMessage);
        assertCommandBehavior("viewall arg not number", expectedMessage);
    }

    @Test
    public void execute_viewAll_invalidIndex() throws Exception {
        assertInvalidIndexBehaviorForCommand("viewall");
    }

    @Test
    public void execute_viewAll_alsoShowsPrivate() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Person p1 = helper.generatePerson(1);
        Person p2 = helper.generatePerson(2);
        List<Person> lastShownList = helper.generatePersonList(p1, p2);
        AddressBook expectedAB = helper.generateAddressBook(lastShownList);
        helper.addToAddressBook(addressBook, lastShownList);

        logic.setLastShownList(lastShownList);

        assertCommandBehavior("viewall 1",
                            String.format(ViewAllCommand.MESSAGE_VIEW_PERSON_DETAILS, p1.getAsTextShowAll()),
                            expectedAB,
                            false,
                            lastShownList);

        assertCommandBehavior("viewall 2",
                            String.format(ViewAllCommand.MESSAGE_VIEW_PERSON_DETAILS, p2.getAsTextShowAll()),
                            expectedAB,
                            false,
                            lastShownList);
    }

    @Test
    public void execute_tryToViewAllPersonMissingInAddressBook_errorMessage() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Person p1 = helper.generatePerson(1);
        Person p2 = helper.generatePerson(2);
        List<Person> lastShownList = helper.generatePersonList(p1, p2);

        AddressBook expectedAB = new AddressBook();
        expectedAB.addPerson(p1);

        addressBook.addPerson(p1);
        logic.setLastShownList(lastShownList);

        assertCommandBehavior("viewall 2",
                                Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK,
                                expectedAB,
                                false,
                                lastShownList);
    }

    /*@Test
    public void execute_delete_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertCommandBehavior("delete ", expectedMessage);
        assertCommandBehavior("delete arg not number", expectedMessage);
    }*/

    @Test
    public void execute_delete_invalidCommandFormat() throws Exception {
        assertInvalidCommandFormatBehaviorForCommand("delete");
    }
//@@author muhdharun -reused
    @Test
    public void execute_delete_removesCorrectPerson() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Person p1 = helper.generatePerson(1);
        Person p2 = helper.generatePerson(2);
        Person p3 = helper.generatePerson(3);

        List<Person> threePersons = helper.generatePersonList(p1, p2, p3);

        AddressBook expectedAB = helper.generateAddressBook(threePersons);
        expectedAB.removePerson(p2);


        helper.addToAddressBook(addressBook, threePersons);
        logic.setLastShownList(threePersons);

        assertCommandBehavior("delete g9999992t",
                                String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, p2),
                                expectedAB,
                                false,
                                threePersons);
    }

    @Test
    public void execute_delete_missingInAddressBook() throws Exception {

        TestDataHelper helper = new TestDataHelper();
        Person p1 = helper.generatePerson(1);
        Person p2 = helper.generatePerson(2);
        Person p3 = helper.generatePerson(3);

        List<Person> threePersons = helper.generatePersonList(p1, p2, p3);

        AddressBook expectedAB = helper.generateAddressBook(threePersons);
        expectedAB.removePerson(p2);

        helper.addToAddressBook(addressBook, threePersons);
        addressBook.removePerson(p2);
        logic.setLastShownList(threePersons);

        assertCommandBehavior("delete g9999992t",
                                Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK,
                                expectedAB,
                                false,
                                threePersons);
    }



//@@author andyrobert3
    @Test
    public void execute_edit_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        assertCommandBehavior("edit ", expectedMessage);
    }

    //@@author andyrobert3
    @Test
    public void execute_edit_invalidCommandFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        assertCommandBehavior("edit ", expectedMessage);
        assertCommandBehavior("edit hello world", expectedMessage);
    }

    //@@author andyrobert3
    @Test
    public void execute_edit_invalidDataFormat() throws Exception {

        TestDataHelper helper = new TestDataHelper();
        Person adam = helper.generatePersonWithNric("s1234567a");
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(adam);


        assertCommandBehavior(
                "edit n/s123456a p/510247 s/wanted w/murder o/gun", NRIC.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "edit n/s1234567a p/50247 s/wanted w/murder o/gun", PostalCode.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "edit n/s1234567a p/123456 s/c w/none", Status.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "edit n/s1234567a p/133456 s/wanted w/ne", Offense.MESSAGE_OFFENSE_INVALID + "\n" + Offense.getListOfValidOffences());
        assertCommandBehavior(
                "edit n/s1234567a p/134546 s/xc w/none o/rr", Offense.MESSAGE_OFFENSE_INVALID + "\n" + Offense.getListOfValidOffences());
    }


    // TODO: HARUN HELP!
//    //@@author andyrobert3

    @Test
    public void execute_edit_successful() throws Exception {
        String nric = "s1234567a";

        TestDataHelper helper = new TestDataHelper();
        Person toBeEdited = helper.generatePersonWithNric(nric);
        addressBook.addPerson(toBeEdited);

        assertCommandBehavior("edit n/s1234567a p/444555 s/xc w/theft o/theft",
                                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, nric),
                                addressBook,
                                false,
                                Collections.emptyList());
    }

    //@@author andyrobert3
    @Test
    public void execute_edit_personNotFound() throws Exception {

        assertCommandBehavior("edit n/s1234567a p/444555 s/clear w/none o/none",
                                Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK,
                                addressBook,
                                false,
                                addressBook.getAllPersons().immutableListView());

        assertCommandBehavior("edit n/f3456789b p/444555 s/xc w/drugs o/drugs",
                                Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK,
                                addressBook,
                                false,
                                Collections.emptyList());

//    @Test
//    public void execute_edit_successful() throws Exception {
//        String nric = "s1234567a";
//
//        TestDataHelper helper = new TestDataHelper();
//        Person toBeEdited = helper.generatePersonWithNric(nric);
//        AddressBook addressBook = new AddressBook();
//        addressBook.addPerson(toBeEdited);
//
//        assertCommandBehavior("edit n/s1234567a p/444555 s/xc w/theft o/theft",
//                                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, nric),
//                                addressBook,
//                                false,
//                                Collections.emptyList());
//    }
//
//    //@@author andyrobert3
//    @Test
//    public void execute_edit_personNotFound() throws Exception {
//        TestDataHelper helper = new TestDataHelper();
//        Person adam = helper.adam();
//        AddressBook expectedAB = new AddressBook();
//        expectedAB.addPerson(adam);
//
//        assertCommandBehavior("edit n/s1234567a p/444555 s/clear w/none o/none",
//                                Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK,
//                                expectedAB,
//                                true,
//                                addressBook.getAllPersons().immutableListView());
//
//        assertCommandBehavior("edit n/f3456789b p/444555 s/xc w/drugs o/drugs",
//                                Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK,
//                                expectedAB,
//                                false,
//                                Collections.emptyList());
//
//    }


    }

    //@@author muhdharun -reused
    @Test
    public void execute_find_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandBehavior("find S1234567A", expectedMessage);
    }

    @Test
    public void execute_find_onlyMatchesFullNric() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Person pTarget1 = helper.generatePersonWithNric("s1234567a");
        Person pTarget2 = helper.generatePersonWithNric("s1234567b");
        Person p1 = helper.generatePersonWithNric("s1234567c");
        Person p2 = helper.generatePersonWithNric("s1234567d");

        List<Person> fourPersons = helper.generatePersonList(p1, pTarget1, p2, pTarget2);
        Person expectedPerson = pTarget2;
        helper.addToAddressBook(addressBook, fourPersons);
        String inputCommand = "find " + pTarget2.getNric().getIdentificationNumber();
        CommandResult r = logic.execute(inputCommand);


        assertEquals(Command.getMessageForPersonShownSummary(expectedPerson), r.feedbackToUser);

    }

    @Test
    public void execute_find_isCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Person pTarget1 = helper.generatePersonWithNric("s1234567b");
        Person pTarget2 = helper.generatePersonWithNric("s1234567c");
        Person p1 = helper.generatePersonWithNric("s1234567d");
        Person p2 = helper.generatePersonWithNric("s1234567e");

        List<Person> fourPersons = helper.generatePersonList(p1, pTarget1, p2, pTarget2);
        helper.addToAddressBook(addressBook, fourPersons);
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandBehavior("find S1234567C",
                expectedMessage,
                addressBook,
                false,
                Collections.emptyList());

    }

//@@author muhdharun
    @Test
    public void execute_check_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CheckCommand.MESSAGE_USAGE);
        assertCommandBehavior("check S1234567A", expectedMessage);
        assertCommandBehavior("check s12345a", expectedMessage);
        assertCommandBehavior("check ", expectedMessage);
    }

    @Test
    public void execute_check_validNric() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Person toBeAdded = helper.generateDummyPerson();
        String nric = toBeAdded.getNric().getIdentificationNumber();

        List<String> emptyTimestamps = new ArrayList<>();
        Formatter formatter = new Formatter();
        String result = formatter.formatForStrings(emptyTimestamps);

        String expectedMessage = result + String.format(MESSAGE_TIMESTAMPS_LISTED_OVERVIEW,nric,emptyTimestamps.size());
        addressBook.addPerson(toBeAdded);
        assertCommandBehavior("check " + nric,
                expectedMessage,
                addressBook,
                false,
                Collections.emptyList());

    }

    @Test
    public void execute_checkPOStatus_CorrectOutput() throws Exception {
        List<String> allPos = CheckPOStatusCommand.extractEngagementInformation(PatrolResourceStatus.getPatrolResourceStatus());
        assertCommandBehavior("checkstatus",
                                Command.getMessage(allPos));
    }

    @Test
    public void execute_updateStatus_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateStatusCommand.MESSAGE_USAGE);
        assertCommandBehavior("updatestatus 111", expectedMessage);
        assertCommandBehavior("updatestatus posq", expectedMessage);
        assertCommandBehavior("updatestatus ", expectedMessage);
    }

    @Test
    public void execute_updateStatus_nonexistingPo() throws Exception {
        String expectedMessage = String.format(Messages.MESSAGE_PO_NOT_FOUND);
        assertCommandBehavior("updatestatus po1234567890123456788", expectedMessage);
    }

    @Test
    public void execute_updateStatus_validPo() throws Exception {
        assertCommandBehavior("updatestatus po1",String.format(UpdateStatusCommand.MESSAGE_UPDATE_PO_SUCCESS,"po1"));
    }

//@@author
//    @Test
//    public void execute_autocorrect_command() throws Exception {
//        CommandResult r =
//
//    }


    //@@author iamputradanish
    @Test
    public void execute_unlockHQP() throws Exception {
        String result = Password.unlockDevice("papa123",5);
        assertEquals(String.format(Password.MESSAGE_WELCOME , Password.MESSAGE_HQP)
                + "\n" + Password.MESSAGE_ENTER_COMMAND , result);
        Password.lockIsHQP();
    }

    @Test
    public void execute_unlockPO() throws Exception {
        String result = Password.unlockDevice("popo1",5);
        assertEquals(String.format(Password.MESSAGE_WELCOME , Password.MESSAGE_PO + Password.MESSAGE_ONE)
                + "\n" + Password.MESSAGE_UNAUTHORIZED
                + "\n" + Password.MESSAGE_ENTER_COMMAND , result);
        Password.lockIsPO();
    }

    @Test
    public void execute_wrongPassword_firstTime() throws Exception{
        Password.lockIsPO();
        Password.lockIsHQP();
        String wrongPassword = "thisiswrong";
        int numberOfAttemptsLeft = 5;
        Password.setWrongPasswordCounter(numberOfAttemptsLeft);
        String result = Password.unlockDevice(wrongPassword, numberOfAttemptsLeft);
        assertEquals(Password.MESSAGE_INCORRECT_PASSWORD
                + "\n" + String.format(Password.MESSAGE_ATTEMPTS_LEFT, numberOfAttemptsLeft)
                + "\n" + MESSAGE_ENTER_PASSWORD,result);
        Password.setWrongPasswordCounter(5);
    }
    @Test
    public void execute_wrongPassword_fourthTime() throws Exception{
        Password.lockIsPO();
        Password.lockIsHQP();
        String wrongPassword = "thisiswrong";
        int numberOfAttemptsLeft = 1;
        Password.setWrongPasswordCounter(numberOfAttemptsLeft);
        String result = Password.unlockDevice(wrongPassword, numberOfAttemptsLeft);
        assertEquals(Password.MESSAGE_INCORRECT_PASSWORD
                + "\n" + String.format(Password.MESSAGE_ATTEMPT_LEFT, numberOfAttemptsLeft)
                + "\n" + Password.MESSAGE_SHUTDOWN_WARNING,result);

    }

    @Test
    public void execute_wrongPassword_lastTime() throws Exception{
        Password.lockIsPO();
        Password.lockIsHQP();
        String wrongPassword = "thisiswrong";
        int numberOfAttemptsLeft = 0;
        Password.setWrongPasswordCounter(numberOfAttemptsLeft);
        String result = Password.unlockDevice(wrongPassword, numberOfAttemptsLeft);
        assertEquals(Password.MESSAGE_SHUTDOWN, result);
    }

    @Test
    public void execute_setWrongPasswordCounter_toPositiveNumber() {
        int randomNumber = ThreadLocalRandom.current().nextInt(0, 6);
        Password.setWrongPasswordCounter(randomNumber);
        assertEquals(randomNumber, Password.getWrongPasswordCounter());
    }

    @Test
    public void execute_setWrongPasswordCounter_toNegativeNumber() {
        int randomNumber = ThreadLocalRandom.current().nextInt(-6, 0);
        Password.setWrongPasswordCounter(randomNumber);
        int result = Password.getWrongPasswordCounter();
        assertEquals(0, result);
    }

    @Test
    public void execute_unlockHQPUser(){
        unlockHQP();
        boolean result = Password.isHQPUser();
        assertTrue(result);
    }

    @Test
    public void execute_unlockPOUser(){
        unlockPO();
        boolean result = Password.isPO();
        assertTrue(result);
    }

    @Test
    public void execute_lockHQPUser(){
        Password.lockIsHQP();
        boolean result = Password.isHQPUser();
        assertFalse(result);
    }

    @Test
    public void execute_lockPOUser(){
        Password.lockIsPO();
        boolean result = Password.isPO();
        assertFalse(result);
    }

    //@@author ongweekeong
    @Test
    public void execute_missingInboxFile() {
        String result = "";
        try{
            ReadNotification testReader = new ReadNotification("Nonsense");
            TreeSet<Msg> testSet = testReader.ReadFromFile();
        }
        catch (IOException e){
            result = MESSAGE_INBOX_FILE_NOT_FOUND;
        }
        assertEquals(MESSAGE_INBOX_FILE_NOT_FOUND, result);
    }

    @Test
    public void execute_readMsgWithoutShowUnread() throws Exception {
        Inbox.numUnreadMsgs = -1; // Set numUnreadMsgs to default state before inbox is accessed.
        String inputCommand = ReadCommand.COMMAND_WORD + " 5";
        String expected = Inbox.INBOX_NOT_READ_YET;
        assertCommandBehavior(inputCommand, expected);
    }

    @Test
    public void execute_checkEmptyInbox() throws Exception{
        WriteNotification.clearInbox(MessageFilePaths.FILEPATH_DEFAULT);
        CommandResult r = logic.execute(InboxCommand.COMMAND_WORD);
        String expectedResult = Messages.MESSAGE_NO_UNREAD_MSGS;
        assertEquals(expectedResult, r.feedbackToUser);
    }

    @Test
    public void execute_checkInboxWithAnUnreadMessage() throws Exception{
        WriteNotification.clearInbox(MessageFilePaths.FILEPATH_DEFAULT);
        String expectedResult = Messages.MESSAGE_UNREAD_MSG_NOTIFICATION+ '\n';
        final String testMessage = "This is a test message.";
        Msg testMsg = generateMsgInInbox(testMessage);
        int messageNum = 1;

        assertCommandBehavior(InboxCommand.COMMAND_WORD, expectedResult, testMsg, messageNum);
    }

    @Test
    public void execute_checkInboxWithMultipleUnreadMessages() throws Exception {
        WriteNotification.clearInbox(MessageFilePaths.FILEPATH_DEFAULT);
        final String testMessage = "This is a test message.";
        Msg testMsg;
        int messageNum = 1, numOfMsgs = 3;
        String expectedResult = Messages.MESSAGE_UNREAD_MSG_NOTIFICATION + '\n';
        //Check that at every additional message added at each loop, the expected result is correct as well.
        while(numOfMsgs!=0) {
            testMsg = generateMsgInInbox(testMessage);

            expectedResult = assertCommandBehavior(InboxCommand.COMMAND_WORD, expectedResult, testMsg, messageNum);

            numOfMsgs--;
            messageNum++;
            Thread.sleep(100);
        }
    }



    @Test
    public void execute_readMsgWithoutUnreadMsgs() throws Exception {
        WriteNotification.clearInbox(MessageFilePaths.FILEPATH_DEFAULT);
        CommandResult r = logic.execute(InboxCommand.COMMAND_WORD);
        String inputCommand = ReadCommand.COMMAND_WORD + " 3";
        String expected = Inbox.INBOX_NO_UNREAD_MESSAGES;
        assertCommandBehavior(inputCommand, expected);
    }

    @Test
    public void execute_readMsgWithOutOfBoundsIndex() throws Exception {
        WriteNotification.clearInbox(MessageFilePaths.FILEPATH_DEFAULT);
        Password.lockIsHQP(); Password.lockIsPO(); //Set static boolean flags from other test cases back to original state.
        Msg testMsg;
        final int numOfMsgs = 3;
        for (int i=0; i<numOfMsgs; i++){
            testMsg = generateMsgInInbox("This is a test message.");
            Thread.sleep(100);
        }
        CommandResult r = logic.execute(InboxCommand.COMMAND_WORD);
        String input1 = ReadCommand.COMMAND_WORD + " 0";
        String expected1 = String.format(Inbox.INDEX_OUT_OF_BOUNDS, numOfMsgs);
        assertCommandBehavior(input1, expected1);
        String input2 = ReadCommand.COMMAND_WORD + " 4";
        String expected2 = String.format(Inbox.INDEX_OUT_OF_BOUNDS, numOfMsgs);
        assertCommandBehavior(input2, expected2);
    }

    @Test
    public void execute_readMsgWithInvalidIndex() throws Exception {
        WriteNotification.clearInbox(MessageFilePaths.FILEPATH_DEFAULT);
        Msg testMsg;
        final int numOfMsgs = 3;
        for(int i=0; i<numOfMsgs; i++){
            testMsg = generateMsgInInbox("This is a test message.");
            Thread.sleep(100);
        }
        String inputCommand = ReadCommand.COMMAND_WORD + " a";
        String expected = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ReadCommand.MESSAGE_USAGE);
        assertCommandBehavior(inputCommand, expected);
    }

    @Test
    public void execute_readMsgWithValidIndex() throws Exception {
        WriteNotification.clearInbox(MessageFilePaths.FILEPATH_DEFAULT);
        Msg testMsg;
        int index = 1;
        final int numOfMsgs = 3;
        for(int i=0; i<numOfMsgs; i++){
            testMsg = generateMsgInInbox("This is a test message. " + index++);
            Thread.sleep(100);
        }
        CommandResult r = logic.execute(InboxCommand.COMMAND_WORD);
        String inputCommand = ReadCommand.COMMAND_WORD + " " + numOfMsgs;
        String expected = ReadCommand.MESSAGE_UPDATE_SUCCESS;
        assertCommandBehavior(inputCommand, expected);
    }

    //@@author

    /**
     * A utility class to generate test data.
     */
    class TestDataHelper{
//@@author muhdharun -reused
        Person adam() throws Exception {
            Name name = new Name("Adam Brown");
            NRIC nric = new NRIC("f1234567j");
            DateOfBirth dateOfBirth = new DateOfBirth("1900");
            PostalCode postalCode = new PostalCode("444444");
            Status status = new Status("xc");
            Offense wantedFor = new Offense();
            Offense tag1 = new Offense("drugs");
            Offense tag2 = new Offense("riot");
            Set<Offense> tags = new HashSet<>(Arrays.asList(tag1, tag2));
            return new Person(name, nric, dateOfBirth, postalCode, status, wantedFor, tags);
        }


        /**
         * Generates a valid person using the given seed.
         * Running this function with the same parameter values guarantees the returned person will have the same state.
         * Each unique seed will generate a unique Person object.
         *
         * @param seed used to generate the person data field values
         *
         */
        Person generatePerson(int seed) throws Exception {
            return new Person(
                    new Name("Person " + seed),
                    new NRIC("g999999" + abs(seed) + "t"),
                    new DateOfBirth(Integer.toString(seed + Integer.parseInt("1901"))),
                    new PostalCode("77777" + seed),
                    new Status("xc"),
                    new Offense(),
                    new HashSet<>(Arrays.asList(new Offense("theft" + abs(seed)), new Offense("theft" + abs(seed + 1))))
            );
        }
//@@author muhdharun
        Person generateDummyPerson() throws Exception {
            return new Person(
                    new Name("Not a human"),
                    new NRIC("f0000000z"),
                    new DateOfBirth("1900"),
                    new PostalCode("777777"),
                    new Status("xc"),
                    new Offense(),
                    new HashSet<>(Arrays.asList(new Offense("theft")))
            );
        }
//@@author muhdharun -reused
        /** Generates the correct add command based on the person given */
        String generateAddCommand(Person p) {
            StringJoiner cmd = new StringJoiner(" ");

            cmd.add("add");

            cmd.add(p.getName().toString());
            cmd.add("n/" + p.getNric());
            cmd.add("d/" + p.getDateOfBirth().getDOB());
            cmd.add("p/" + p.getPostalCode());
            cmd.add("s/" + p.getStatus());
            cmd.add("w/" + p.getWantedFor().getOffense());

            Set<Offense> tags = p.getPastOffenses();
            for(Offense t: tags){
                cmd.add("o/" + t.getOffense());
            }

            return cmd.toString();
        }
//@@author
        /**
         * Generates an AddressBook with auto-generated persons.
         * @param isPrivateStatuses flags to indicate if all contact details of respective persons should be set to
         *                          private.
         */
        AddressBook generateAddressBook(Boolean... isPrivateStatuses) throws Exception{
            AddressBook addressBook = new AddressBook();
            addToAddressBook(addressBook, isPrivateStatuses);
            return addressBook;
        }

        /**
         * Generates an AddressBook based on the list of Persons given.
         */
        AddressBook generateAddressBook(List<Person> persons) throws Exception{
            AddressBook addressBook = new AddressBook();
            addToAddressBook(addressBook, persons);
            return addressBook;
        }

        /**
         * Adds auto-generated Person objects to the given AddressBook
         * @param addressBook The AddressBook to which the Persons will be added
         * @param isPrivateStatuses flags to indicate if all contact details of generated persons should be set to
         *                          private.
         */
        void addToAddressBook(AddressBook addressBook, Boolean... isPrivateStatuses) throws Exception{
            addToAddressBook(addressBook, generatePersonList(isPrivateStatuses));
        }

        /**
         * Adds the given list of Persons to the given AddressBook
         */
        void addToAddressBook(AddressBook addressBook, List<Person> personsToAdd) throws Exception{
            for(Person p: personsToAdd){
                addressBook.addPerson(p);
            }
        }

        /**
         * Creates a list of Persons based on the give Person objects.
         */
        List<Person> generatePersonList(Person... persons) throws Exception{
            List<Person> personList = new ArrayList<>();
            for(Person p: persons){
                personList.add(p);
            }
            return personList;
        }

        /**
         * Generates a list of Persons based on the flags.
         * @param isPrivateStatuses flags to indicate if all contact details of respective persons should be set to
         *                          private.
         */
        List<Person> generatePersonList(Boolean... isPrivateStatuses) throws Exception{
            List<Person> persons = new ArrayList<>();
            int i = 1;
            for(Boolean p: isPrivateStatuses){
                persons.add(generatePerson(i++));
            }
            return persons;
        }

        /**
         * Generates a random NRIC
         */
        //@@author muhdharun
        String generateRandomNric() {
            int min = 1111111;
            int max = 9999999;
            Random r = new Random();
            return "s"+Integer.toString(r.nextInt((max - min) + 1) + min)+"a";
        }

        /**
         * Generates a Person object with given nric. Other fields will have some dummy values.
         */

        Person generatePersonWithNric(String nric) throws Exception {
            return new Person(
                    new Name("Bob"),
                    new NRIC(nric),
                    new DateOfBirth("2005"),
                    new PostalCode("123456"),
                    new Status("xc"),
                    new Offense(),
                    Collections.singleton(new Offense("riot"))
            );
        }
        //@@author muhdharun -reused
        /**
         * Generates a Person object with given name. Other fields will have some dummy values.
         */
         Person generatePersonWithName(String name) throws Exception {
            String randomNric = generateRandomNric();
            return new Person(
                    new Name(name),
                    new NRIC(randomNric),
                    new DateOfBirth("2005"),
                    new PostalCode("123456"),
                    new Status("xc"),
                    new Offense(),
                    Collections.singleton(new Offense("riot"))
            );
        }
        //@@author ongweekeong

        /**
         * Generates a Msg object with given message. Other fields will have some dummy values.
         * @param message
         * @return
         */
        Msg generateUnreadMsgNoLocation(String message, Msg.Priority urgency){
             return new Msg(urgency, message);
        }

        Msg generateUnreadMsgWithLocation(String message, Msg.Priority urgency, Location location){
            return new Msg(urgency, message, location);
        }

        Location generateRandomLocation(){
            return new Location(1.294166, 103.770730);
        }

    }

    //@@author ongweekeong
    Timestamp adjustExpectedTimestamp(String message, int msgNum) throws Exception {
        SimpleDateFormat timeFormatted = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss:SSS");
        Date formattedTime = timeFormatted.parse(parseMsgForDateTimeStamp(message, msgNum));
        Timestamp newTime = new Timestamp(formattedTime.getTime());
        return newTime;
    }
    Msg generateMsgInInbox(String testMessage) throws Exception {
        TestDataHelper MessageGenerator = new TestDataHelper();
        Msg testMsg = MessageGenerator.generateUnreadMsgNoLocation(testMessage, Msg.Priority.HIGH);
        WriteNotification testWriter = new WriteNotification(MessageFilePaths.FILEPATH_DEFAULT, true);
        testWriter.writeToFile(testMsg);
        return testMsg;
    }
    String parseMsgForTimestamp(String message){
        //int limit = 1 + msgNum;
        String[] timestamp = message.split("-", 2);
        String time = timestamp[1].substring(0,12);
        return time;
    }
    String parseMsgForDateTimeStamp (String message, int msgNum){
        int limit = msgNum + 1;
        String[] timestamp = message.split("Sent: ",limit);
        String date = timestamp[msgNum].substring(0,23);
        return date;
    }

}
