# andyrobert3
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
    @Test
    public void execute_request_invalidOffense() throws Exception {
        String expectedMessage =  Offense.MESSAGE_OFFENSE_INVALID;
        assertCommandBehavior("rb crime", expectedMessage);
        assertCommandBehavior("rb tired", expectedMessage);
    }

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
    @Test
    public void execute_edit_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        assertCommandBehavior("edit ", expectedMessage);
    }

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
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(adam);


        assertCommandBehavior(
                "edit n/s123456a p/510247 s/wanted w/murder o/gun", NRIC.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "edit n/s1234567a p/50247 s/wanted w/murder o/gun", PostalCode.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "edit n/s1234567a p/123456 s/c w/none", Status.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "edit n/s1234567a p/133456 s/wanted w/ne", Offense.MESSAGE_OFFENSE_INVALID);
        assertCommandBehavior(
                "edit n/s1234567a p/134546 s/xc w/none o/rr", Offense.MESSAGE_OFFENSE_INVALID);
    }


    // TODO: HARUN HELP!
```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java

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

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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


    }

```
