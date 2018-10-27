# iamputradanish
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java

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

```
###### \java\seedu\addressbook\logic\LogicTest.java
``` java
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

```
