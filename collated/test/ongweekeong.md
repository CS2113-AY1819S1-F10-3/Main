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
        String[] inputTime = parts[1].split(":", 4);
        String[] expectedTime = expected[1].split(":", 4);
        inputTime[3] = inputTime[3].substring(0, 2);
        expectedTime[3] = expectedTime[3].substring(0, 2);
        int difference = abs(Integer.parseInt(inputTime[3]) - Integer.parseInt(expectedTime[3]));
        if(inputTime[0].equals(expectedTime[0]) && inputTime[1].equals(expectedTime[1]) && inputTime[2].equals(expectedTime[2]) &&
                (difference <= tolerance) && parts[0].equals(expected[0])){
            expectedMessage = r.feedbackToUser;
        }
        //assertEquals(expected[0], parts[0]);
        assertEquals(expectedMessage, r.feedbackToUser);
    }

```
