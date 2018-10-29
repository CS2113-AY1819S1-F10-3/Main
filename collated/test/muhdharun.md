# muhdharun
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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
```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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



```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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
```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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
```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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
```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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
```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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
```
###### \java\seedu\addressbook\parser\ParserTest.java
``` java
    @Test
    public void checkPoStatusCommand_parsedCorrectly() {
        final String input = "checkstatus";
        parseAndAssertCommandType(input, CheckPOStatusCommand.class);
    }
    /**
     * Test single argument commands
     */
    @Test
    public void updateStatusCommand_parsedCorrectly() {
        final String input = "updatestatus po1";
        parseAndAssertCommandType(input,UpdateStatusCommand.class);
    }

    @Test
    public void updateStatusCommand_invalidPoArg() {
        final String input = "updatestatus ppp";
        String resultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateStatusCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage,input);
    }

    @Test
    public void updateStatusCommand_noArg() {
        final String input = "updatestatus";
        String resultMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateStatusCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage,input);
    }

```
###### \java\seedu\addressbook\parser\ParserTest.java
``` java
    @Test
    public void findCommand_validArgs_parsedCorrectly() {
        final String keyword = "s1234567a";
        final String input = "find " + keyword;
        final FindCommand result =
                parseAndAssertCommandType(input, FindCommand.class);
        assertEquals(keyword, result.getNric());
    }

    @Test
    public void findCommand_duplicateKeys_parsedCorrectly() {
        final String keyword = "s1234567a";
        // duplicate every keyword
        final String input = "find " + keyword + " " + keyword;
        final String resultMessage =
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        parseAndAssertIncorrectWithMessage(resultMessage, input);
    }

    /**
     * Test check persons by nric command
     */
```
###### \java\seedu\addressbook\parser\ParserTest.java
``` java
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
```
###### \java\seedu\addressbook\parser\ParserTest.java
``` java
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
```
###### \java\seedu\addressbook\parser\ParserTest.java
``` java
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
```
