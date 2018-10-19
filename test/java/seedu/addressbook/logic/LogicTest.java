package seedu.addressbook.logic;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import seedu.addressbook.commands.*;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.person.*;
import seedu.addressbook.password.Password;
import seedu.addressbook.storage.StorageFile;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static seedu.addressbook.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.addressbook.common.Messages.MESSAGE_TIMESTAMPS_LISTED_OVERVIEW;


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

    @Test
    public void execute_unknownCommandWord_forPO() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        Password.unlockPO();
        assertCommandBehavior(unknownCommand, HelpCommand.MESSAGE_PO_USAGES);
        Password.lockIsPO();
    }

    @Test
    public void execute_help_forPO() throws Exception {
        Password.unlockPO();
        assertCommandBehavior("help", HelpCommand.MESSAGE_PO_USAGES);
        Password.lockIsPO();
    }

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
                "add Valid Name n/s1234567a d/1980 p/123456 s/xc w/none o/rob", Offense.MESSAGE_OFFENSE_INVALID);
        assertCommandBehavior(
                "add Valid Name n/s1234567a d/1980 p/123456 s/wanted w/none o/none", Person.WANTED_FOR_WARNING);

    }

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
    /*
    @Test
    public void execute_view_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewAllCommand.MESSAGE_USAGE);
        assertCommandBehavior("view ", expectedMessage);
        assertCommandBehavior("view arg not number", expectedMessage);
    }
    */

    /*@Test
    public void execute_view_invalidIndex() throws Exception {
        assertInvalidIndexBehaviorForCommand("view");
    }*/

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

    //@Test
   /*
    public void execute_view_onlyShowsNonPrivate() throws Exception {

        TestDataHelper helper = new TestDataHelper();
        Person p1 = helper.generatePerson(1);
        Person p2 = helper.generatePerson(2);
        List<Person> lastShownList = helper.generatePersonList(p1, p2);
        AddressBook expectedAB = helper.generateAddressBook(lastShownList);
        helper.addToAddressBook(addressBook, lastShownList);

        logic.setLastShownList(lastShownList);

        assertCommandBehavior("view 1",
                              String.format(ViewCommand.MESSAGE_VIEW_PERSON_DETAILS, p1.getAsTextHidePrivate()),
                              expectedAB,
                              false,
                              lastShownList);

        assertCommandBehavior("view 2",
                              String.format(ViewCommand.MESSAGE_VIEW_PERSON_DETAILS, p2.getAsTextHidePrivate()),
                              expectedAB,
                              false,
                              lastShownList);
    }
*/
   /*
   @Test
    public void execute_tryToViewMissingPerson_errorMessage() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Person p1 = helper.generatePerson(1);
        Person p2 = helper.generatePerson(2);
        List<Person> lastShownList = helper.generatePersonList(p1, p2);

        AddressBook expectedAB = new AddressBook();
        expectedAB.addPerson(p2);

        addressBook.addPerson(p2);
        logic.setLastShownList(lastShownList);

        assertCommandBehavior("view 1",
                              Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK,
                              expectedAB,
                              false,
                              lastShownList);
    }
    */

    @Test
    public void execute_request_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RequestHelp.MESSAGE_USAGE);
        assertCommandBehavior("request ", expectedMessage);
        assertCommandBehavior("request gun", expectedMessage);
    }

    @Test
    public void execute_request_invalidOffense() throws Exception {
        String expectedMessage = String.format(Offense.MESSAGE_OFFENSE_INVALID);
        assertCommandBehavior("request bobo help", expectedMessage);
        assertCommandBehavior("request lala help", expectedMessage);
    }


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

    @Test
    public void execute_edit_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        assertCommandBehavior("edit ", expectedMessage);
    }

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
        AddressBook expectedAB = helper.generateAddressBook(fourPersons);
        List<Person> expectedList = helper.generatePersonList(pTarget2);
        helper.addToAddressBook(addressBook, fourPersons);

        assertCommandBehavior("find s1234567b",
                                Command.getMessageForPersonListShownSummary(expectedList),
                                expectedAB,
                                true,
                                expectedList);
    }

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
        logic.execute(helper.generateAddCommand(toBeAdded));
        CommandResult r = logic.execute("check " + nric);
        String message = r.feedbackToUser.trim();
        String expectedMessage = String.format(MESSAGE_TIMESTAMPS_LISTED_OVERVIEW,0);
        assertEquals(expectedMessage,message);
        logic.execute("delete " + nric);

    }

//    @Test
//    public void execute_autocorrect_command() throws Exception {
//        CommandResult r =
//
//    }

//    @Test
//    public void execute_find_isCaseSensitive() throws Exception {
//        TestDataHelper helper = new TestDataHelper();
//        Person pTarget1 = helper.generatePersonWithNric("s1234567b");
//        Person pTarget2 = helper.generatePersonWithNric("s1234567c");
//        Person p1 = helper.generatePersonWithNric("s1234567d");
//        Person p2 = helper.generatePersonWithNric("s1234567e");
//
//        List<Person> fourPersons = helper.generatePersonList(p1, pTarget1, p2, pTarget2);
//        AddressBook expectedAB = helper.generateAddressBook(fourPersons);
//        List<Person> expectedList = helper.generatePersonList(pTarget1, pTarget2);
//        helper.addToAddressBook(addressBook, fourPersons);
//        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
//        assertCommandBehavior("find S1234567B",
//                                Command.getMessageForPersonListShownSummary(expectedList),
//                                expectedAB,
//                                true,
//                                expectedList);
//
//    }

//    @Test
//    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
//        TestDataHelper helper = new TestDataHelper();
//        Person pTarget1 = helper.generatePersonWithName("bla bla KEY bla");
//        Person pTarget2 = helper.generatePersonWithName("bla rAnDoM bla bceofeia");
//        Person p1 = helper.generatePersonWithName("key key");
//        Person p2 = helper.generatePersonWithName("KEy sduauo");
//
//        List<Person> fourPersons = helper.generatePersonList(p1, pTarget1, p2, pTarget2);
//        AddressBook expectedAB = helper.generateAddressBook(fourPersons);
//        List<Person> expectedList = helper.generatePersonList(pTarget1, pTarget2);
//        helper.addToAddressBook(addressBook, fourPersons);
//
//        assertCommandBehavior("find KEY rAnDoM",
//                                Command.getMessageForPersonListShownSummary(expectedList),
//                                expectedAB,
//                                true,
//                                expectedList);
//    }

    @Test
    public void execute_unlockHQP() throws Exception {
        String result = Password.unlockDevice("mama123",5);
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



    /**
     * A utility class to generate test data.
     */
    class TestDataHelper{

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
                    new NRIC("g999999" + Math.abs(seed) + "t"),
                    new DateOfBirth(Integer.toString(seed + Integer.parseInt("1901"))),
                    new PostalCode("77777" + seed),
                    new Status("xc"),
                    new Offense(),
                    new HashSet<>(Arrays.asList(new Offense("theft" + Math.abs(seed)), new Offense("theft" + Math.abs(seed + 1))))
            );
        }

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
    }

}
