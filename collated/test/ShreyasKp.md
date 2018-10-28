# ShreyasKp
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
    @Test
    public void execute_timeCommand() throws Exception {
        String command = DateTimeCommand.COMMAND_WORD;
        TimeAndDate timeAndDate = new TimeAndDate();
        assertCommandBehavior(command, timeAndDate.outputDATHrs(), 200);
    }

```
###### \java\seedu\addressbook\parser\ParserTest.java
``` java
    @Test
    public void dateTimeCommand_parsedCorrectly() {
        final String input = DateTimeCommand.COMMAND_WORD;
        parseAndAssertCommandType(input, DateTimeCommand.class);
    }

```
