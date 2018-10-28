# ongweekeong
###### \java\seedu\addressbook\logic\LogicTest.java
``` java

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

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java

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

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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
```
