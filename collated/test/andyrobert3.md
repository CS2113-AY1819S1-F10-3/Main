# andyrobert3
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
    @Test
    public void execute_request_invalidOffense() throws Exception {
        String expectedMessage = String.format(Offense.MESSAGE_OFFENSE_INVALID);
        assertCommandBehavior("request bobo help", expectedMessage);
        assertCommandBehavior("request lala help", expectedMessage);
    }

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
    @Test
    public void execute_edit_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        assertCommandBehavior("edit ", expectedMessage);
    }

//    @Test
//    public void execute_edit_invalidPersonData() throws Exception {
//        assertCommandBehavior(
//                "add []\\[;] n/s1234567a d/1980 p/123456 s/clear w/none", Name.MESSAGE_NAME_CONSTRAINTS);
//        assertCommandBehavior(
//                "add Valid Name n/s123457a d/1980 p/123456 s/clear w/none", NRIC.MESSAGE_NAME_CONSTRAINTS);
//        assertCommandBehavior(
//                "add Valid Name n/s1234567a d/188 p/123456 s/clear w/none", DateOfBirth.MESSAGE_DATE_OF_BIRTH_CONSTRAINTS);
//        assertCommandBehavior(
//                "add Valid Name n/s1234567a d/1980 p/13456 s/clear w/none", PostalCode.MESSAGE_NAME_CONSTRAINTS);
//        assertCommandBehavior(
//                "add Valid Name n/s1234567a d/1980 p/123456 s/xc w/none o/rob", Offense.MESSAGE_OFFENSE_INVALID);
//        assertCommandBehavior(
//                "add Valid Name n/s1234567a d/1980 p/123456 s/wanted w/none o/none", Person.WANTED_FOR_WARNING);
//    }
```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
    @Test
    public void execute_edit_invalidCommandFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        assertCommandBehavior("edit ", expectedMessage);
        assertCommandBehavior("edit hello world", expectedMessage);
    }

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
    @Test
    public void execute_edit_invalidDataFormat() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Person adam = helper.generatePersonWithNric("s1234567a");
        AddressBook expectedAB = new AddressBook();
        expectedAB.addPerson(adam);

        assertCommandBehavior(
                "edit n/s6789 p/510247 s/wanted w/murder o/gun", NRIC.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "edit n/s1234567a p/100 s/wanted w/murder o/gun", PostalCode.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "edit n/s1234567a p/510247 s/clear w/none o/rob", Offense.MESSAGE_OFFENSE_INVALID);
    }


    // TODO: HARUN HELP!
```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
//    @Test
//    public void execute_edit_successful() throws Exception {
//        String nric = "s1234567a";
//
//        TestDataHelper helper = new TestDataHelper();
//        Person toBeEdited = helper.generatePersonWithNric(nric);
//        AddressBook expectedAB = new AddressBook();
//        expectedAB.addPerson(toBeEdited);
//
//        assertCommandBehavior("edit n/s1234567a p/444555 s/xc w/theft o/theft",
//                                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, nric),
//                                expectedAB,
//                                false,
//                                Collections.emptyList());
//    }
//
```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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
        AddressBook expectedAB = helper.generateAddressBook(fourPersons);
        List<Person> expectedList = helper.generatePersonList(pTarget1, pTarget2);
        helper.addToAddressBook(addressBook, fourPersons);
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        CommandResult r = logic.execute("find " + "S1234567B");
        assertEquals(expectedMessage,r.feedbackToUser);

    }

```
