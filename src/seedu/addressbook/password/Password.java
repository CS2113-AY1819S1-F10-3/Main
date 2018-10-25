package seedu.addressbook.password;

import seedu.addressbook.common.Messages;
import seedu.addressbook.readandwrite.ReaderAndWriter;

import java.io.*;

//@@author iamputradanish
public class Password {

    private static final String MESSAGE_TRY_AGAIN = "Please try again.";
    public static final String MESSAGE_ENTER_PASSWORD = "Please enter password: ";
    public static final String MESSAGE_ENTER_COMMAND = "Please enter a command: ";
    public static final String MESSAGE_WELCOME = "Welcome %s.";
    public static final String MESSAGE_UNAUTHORIZED = "You are not authorized to ADD, CLEAR, CHECK, DELETE, EDIT nor UPDATE PASSWORD.";
    public static final String MESSAGE_INCORRECT_PASSWORD = "Password is incorrect. " + MESSAGE_TRY_AGAIN;
    public static final String MESSAGE_ATTEMPTS_LEFT = "You have %1$d attempts left. ";
    public static final String MESSAGE_ATTEMPT_LEFT = "You have %1$d attempt left. ";
    public static final String MESSAGE_SHUTDOWN_WARNING = "System will shut down if password is incorrect. ";
    public static final String MESSAGE_SHUTDOWN = "Password is incorrect. System will shut down. ";
    private static final String MESSAGE_ENTER_PASSWORD_TO_CHANGE = "Please enter current password to change: ";
    public static final String MESSAGE_HQP = "Headquarters Personnel";
    public static final String MESSAGE_PO = "Police Officer ";
    public static final String MESSAGE_ONE = "Oscar November Echo";
    public static final String MESSAGE_TWO = "Tango Whiskey Oscar";
    public static final String MESSAGE_THREE = "Tango Hotel Romeo Echo Echo";
    public static final String MESSAGE_FOUR = "Foxtrot Oscar Uniform Romeo";
    public static final String MESSAGE_FIVE = "Foxtrot India Victor Echo";
    private static final String MESSAGE_ENTER_NEW_PASSWORD = "Please enter new alphanumeric password for ";
    private static final String MESSAGE_ENTER_NEW_PASSWORD_AGAIN = "Please enter new alphanumeric password again: ";
    private static final String MESSAGE_UPDATED_PASSWORD = "You have updated %s password successfully. ";
    private static final String MESSAGE_NOT_SAME = "The password you entered is not the same. ";
    private static final String MESSAGE_AT_LEAST_ONE = "Your new password must contain at least one %s. ";
    private static final String MESSAGE_PASSWORD_EXISTS = "Your new password cannot be the same as an existing password. ";
    private static final String MESSAGE_TRY_UNAUTHORIZED ="You are unauthorized to %s.\nPlease try a different command. ";
    private static final String MESSAGE_PASSWORD_LENGTH = "Your new password is %1$d character(s) long. ";
    private static final String MESSAGE_PASSWORD_MINIMUM_LENGTH = "Your new password must be at least %1$d characters long. ";
    public static final String UPDATE_PASSWORD_COMMAND_WORD = "update password";
    public static final String UPDATE_PASSWORD_MESSAGE_USAGE = UPDATE_PASSWORD_COMMAND_WORD + ":\n" + "Updates a password\n\t"
            + "Example: " + UPDATE_PASSWORD_COMMAND_WORD;


    public static int getWrongPasswordCounter() {
        return wrongPasswordCounter;
    }
    public static void setWrongPasswordCounter(int number) {
        wrongPasswordCounter = (number >= 0) ? number:0;
    }
    private static int wrongPasswordCounter = 5;


    private static boolean isHQP = false;
    public static void unlockHQP(){
        isHQP = true;
    }
    public static void unlockPO(){
        isPO1 = true;
        isPO2 = true;
        isPO3 = true;
        isPO4 = true;
        isPO5 = true;
    }
    private static boolean isPO1 = false;
    private static boolean isPO2 = false;
    private static boolean isPO3 = false;
    private static boolean isPO4 = false;
    private static boolean isPO5 = false;
    public static void lockIsHQP() {
        isHQP = false;
    }
    public static void lockIsPO() {
        isPO1 = false;
        isPO2 = false;
        isPO3 = false;
        isPO4 = false;
        isPO5 = false;
    }
    public static boolean isHQPUser() {
        return isHQP;
    }
    public static boolean isLocked(){
        return !(isHQP || isPO());
    }

    public static boolean isPO(){
        return (isPO1 || isPO2 || isPO3 || isPO4 || isPO5);
    }

    public boolean isUpdatingPasswordNow() {
        return isUpdatingPassword;
    }

    private void lockUpdatingPassword() {
        isUpdatingPassword = false;
    }

    private void lockUpdatePasswordConfirm() {
        isUpdatePasswordConfirm = false;
    }

    public void lockDevice(){
        lockIsHQP();
        lockIsPO();
        lockUpdatePasswordConfirm();
        lockUpdatingPassword();
    }
    private boolean isUpdatingPassword = false;
    public boolean isUpdatePasswordConfirmNow() {
        return isUpdatePasswordConfirm;
    }

    private boolean isUpdatePasswordConfirm = false;

    private boolean isNotLogin(){
        return (!isLoginHQP && !isLoginPO());
    }
    private boolean isLoginPO(){
        return (isLoginPO1 || isLoginPO2 || isLoginPO3 || isLoginPO4 || isLoginPO5);
    }
    private boolean isLoginHQP = false;
    private boolean isLoginPO1 = false;
    private boolean isLoginPO2 = false;
    private boolean isLoginPO3 = false;
    private boolean isLoginPO4 = false;
    private boolean isLoginPO5 = false;

    public boolean isShutDown() {
        return isShutDown;
    }

    private static boolean isShutDown= false;
    private String oneTimePassword = null;

    private static ReaderAndWriter readerandwriter = new ReaderAndWriter();

    public static String unlockDevice(String userCommandText, int number) throws IOException {

        String result = null;

        File originalFile = readerandwriter.fileToUse("passwordStorage.txt");
        BufferedReader br = readerandwriter.openReader(originalFile);


        int hashedEnteredPassword= userCommandText.hashCode();

        String line;
        int numberOfPasswords = 6;

        while (numberOfPasswords > 0) {
            line = br.readLine();
            String storedCurrPassword = line.substring(line.lastIndexOf(" ") + 1);
            String user = line.substring(0,line.lastIndexOf(" "));

            if (correctHQP(user,storedCurrPassword,hashedEnteredPassword)) {
                isHQP = true;
                result = String.format(MESSAGE_WELCOME, MESSAGE_HQP) + "\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else if (correctPO1(user,storedCurrPassword,hashedEnteredPassword)) {
                isPO1 = true;
                result = String.format(MESSAGE_WELCOME, MESSAGE_PO + MESSAGE_ONE) + "\n"
                        + MESSAGE_UNAUTHORIZED + "\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else if (correctPO2(user,storedCurrPassword,hashedEnteredPassword)) {
                isPO2 = true;
                result = String.format(MESSAGE_WELCOME, MESSAGE_PO + MESSAGE_TWO) + "\n"
                        + MESSAGE_UNAUTHORIZED + "\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else if (correctPO3(user,storedCurrPassword,hashedEnteredPassword)) {
                isPO3 = true;
                result = String.format(MESSAGE_WELCOME, MESSAGE_PO + MESSAGE_THREE) + "\n"
                        + MESSAGE_UNAUTHORIZED + "\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else if (correctPO4(user,storedCurrPassword,hashedEnteredPassword)) {
                isPO4 = true;
                result = String.format(MESSAGE_WELCOME, MESSAGE_PO + MESSAGE_FOUR) + "\n"
                        + MESSAGE_UNAUTHORIZED + "\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else if (correctPO5(user,storedCurrPassword,hashedEnteredPassword)) {
                isPO5 = true;
                result = String.format(MESSAGE_WELCOME, MESSAGE_PO + MESSAGE_FIVE) + "\n"
                        + MESSAGE_UNAUTHORIZED + "\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else{
                result = Messages.MESSAGE_ERROR;
            }
            numberOfPasswords--;
        }
        if(isLocked()){
            result = wrongPasswordShutDown(number);
        }
        else{
            isShutDown = false;
        }
        br.close();
        return result;
    }

    private static String wrongPasswordShutDown(int number){
        String result;
        if(wrongPasswordCounter>1) {
            result = MESSAGE_INCORRECT_PASSWORD
                    + "\n" + String.format(MESSAGE_ATTEMPTS_LEFT,number)
                    + "\n" + MESSAGE_ENTER_PASSWORD;
            decreaseWrongPasswordCounter();
        }
        else if(wrongPasswordCounter == 1){
            result = MESSAGE_INCORRECT_PASSWORD
                    + "\n" + String.format(MESSAGE_ATTEMPT_LEFT,number)
                    + "\n" + MESSAGE_SHUTDOWN_WARNING;
            decreaseWrongPasswordCounter();
        }
        else if(wrongPasswordCounter == 0){
            isShutDown = true;
            result = MESSAGE_SHUTDOWN;
        }
        else{
            result = Messages.MESSAGE_ERROR;
        }
        return result;
    }

    private static void decreaseWrongPasswordCounter(){
        wrongPasswordCounter--;
    }

    private static boolean correctPassword(String storedCurrPassword, int hashedEnteredPassword){
        return storedCurrPassword.equals(Integer.toString(hashedEnteredPassword));
    }

    private static boolean correctHQP(String user, String storedCurrPassword, int hashedEnteredPassword){
        return user.equals("hqp") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }

    private static boolean correctPO1(String user, String storedCurrPassword, int hashedEnteredPassword){
        return user.equals("po1") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }
    private static boolean correctPO2(String user, String storedCurrPassword, int hashedEnteredPassword){
        return user.equals("po2") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }
    private static boolean correctPO3(String user, String storedCurrPassword, int hashedEnteredPassword){
        return user.equals("po3") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }
    private static boolean correctPO4(String user, String storedCurrPassword, int hashedEnteredPassword){
        return user.equals("po4") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }
    private static boolean correctPO5(String user, String storedCurrPassword, int hashedEnteredPassword){
        return user.equals("po5") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }

    public String prepareUpdatePassword(){
        isUpdatingPassword = true;
        setWrongPasswordCounter(5);
        return MESSAGE_ENTER_PASSWORD_TO_CHANGE;
    }

    public String updatePassword(String userCommandText,int number) throws Exception {

        String result = null;

        File originalFile = readerandwriter.fileToUse("passwordStorage.txt");
        BufferedReader br = readerandwriter.openReader(originalFile);

        String line;

        if(isNotLogin()){

            int enteredCurrentPassword = userCommandText.hashCode();

            int numberOfPasswords = 6;

            while(numberOfPasswords>0) {
                line = br.readLine();
                String storedCurrPassword = line.substring(line.lastIndexOf(" ") + 1);
                String user = line.substring(0,line.lastIndexOf(" "));

                if(correctPassword(storedCurrPassword, enteredCurrentPassword)){
                    setWrongPasswordCounter(5);

                    switch (user) {
                        case "hqp":
                            isLoginHQP = true;
                            result = MESSAGE_ENTER_NEW_PASSWORD + MESSAGE_HQP + ":";
                            break;
                        case "po1":
                            isLoginPO1 = true;
                            result = MESSAGE_ENTER_NEW_PASSWORD + MESSAGE_PO + MESSAGE_ONE + ":";
                            break;
                        case "po2":
                            isLoginPO2 = true;
                            result = MESSAGE_ENTER_NEW_PASSWORD + MESSAGE_PO + MESSAGE_TWO + ":";
                            break;
                        case "po3":
                            isLoginPO3 = true;
                            result = MESSAGE_ENTER_NEW_PASSWORD + MESSAGE_PO + MESSAGE_THREE + ":";
                            break;
                        case "po4":
                            isLoginPO4 = true;
                            result = MESSAGE_ENTER_NEW_PASSWORD + MESSAGE_PO + MESSAGE_FOUR + ":";
                            break;
                        case "po5":
                            isLoginPO5 = true;
                            result = MESSAGE_ENTER_NEW_PASSWORD + MESSAGE_PO + MESSAGE_FIVE + ":";
                            break;
                    }

                }
                numberOfPasswords--;
            }
            if(isNotLogin()){
                result = wrongPasswordShutDown(number);
            }
        }
        else{
            result = passwordValidityChecker(userCommandText);
            if(result == null){
                oneTimePassword = userCommandText;
                isUpdatePasswordConfirm = true;
                result = MESSAGE_ENTER_NEW_PASSWORD_AGAIN;
            }
        }
        br.close();
        return result;
    }

    private boolean isEqualPassword (String secondPassword){
        return secondPassword.equals(oneTimePassword);
    }

    public String updatePasswordFinal (String userCommandText) throws IOException {

        String result = null;
        int lineNumber =0 , linesLeft;

        File originalFile = readerandwriter.fileToUse("passwordStorage.txt");
        BufferedReader br = readerandwriter.openReader(originalFile);
        File tempFile = readerandwriter.fileToUse("tempFile.txt");
        PrintWriter pw = readerandwriter.openTempWriter(tempFile);

        String line;

        if(isEqualPassword(userCommandText)) {
            int storedNewPassword = userCommandText.hashCode();

            if(isLoginHQP){
                isLoginHQP = false;
                result = String.format(MESSAGE_UPDATED_PASSWORD,MESSAGE_HQP);
            }
            else if(isLoginPO1){
                lineNumber = 1;
                isLoginPO1 = false;
                result = String.format(MESSAGE_UPDATED_PASSWORD,MESSAGE_PO + MESSAGE_ONE);
            }
            else if(isLoginPO2){
                lineNumber = 2;
                isLoginPO2 = false;
                result = String.format(MESSAGE_UPDATED_PASSWORD,MESSAGE_PO + MESSAGE_TWO);
            }
            else if(isLoginPO3){
                lineNumber = 3;
                isLoginPO3 = false;
                result = String.format(MESSAGE_UPDATED_PASSWORD,MESSAGE_PO + MESSAGE_THREE);
            }
            else if(isLoginPO4){
                lineNumber = 4;
                isLoginPO4 = false;
                result = String.format(MESSAGE_UPDATED_PASSWORD,MESSAGE_PO + MESSAGE_FOUR);
            }
            else if(isLoginPO5){
                lineNumber = 5;
                isLoginPO5 = false;
                result = String.format(MESSAGE_UPDATED_PASSWORD,MESSAGE_PO + MESSAGE_FIVE);
            }
            isUpdatingPassword = false;
            isUpdatePasswordConfirm = false;
            linesLeft = 5 - lineNumber;
            while (lineNumber > 0){
                reprintLine(br,pw);
                lineNumber--;
            }
            line = br.readLine();
            line = line.substring(0, line.lastIndexOf(" ") + 1) + Integer.toString(storedNewPassword);
            pw.println(line);
            pw.flush();
            while (linesLeft > 0){
                reprintLine(br,pw);
                linesLeft--;
            }
            pw.close();
            br.close();

            if (!originalFile.delete()) {
                result = (Messages.MESSAGE_ERROR);
            }
            if (!tempFile.renameTo(originalFile)) {
                result = (Messages.MESSAGE_ERROR);
            }
            result = result +
                    "\n" + MESSAGE_ENTER_COMMAND;
        }
        else{
            isUpdatePasswordConfirm = false;
            result = MESSAGE_NOT_SAME;
        }
        pw.close();
        br.close();
        return result;
    }

    private void reprintLine(BufferedReader br, PrintWriter pw) throws IOException {
        String line = br.readLine();
        pw.println(line);
        pw.flush();
    }

    private String passwordExistsChecker (String newEnteredPassword) throws IOException {
        String result = null;

        File originalFile = readerandwriter.fileToUse("passwordStorage.txt");
        BufferedReader br = readerandwriter.openReader(originalFile);

        int hashedEnteredPassword = newEnteredPassword.hashCode();

        String line;
        int numberOfPasswords = 6;

        while (numberOfPasswords > 0) {
            line = br.readLine();
            String storedCurrPassword = line.substring(line.lastIndexOf(" ") + 1);
            if (correctPassword(storedCurrPassword, hashedEnteredPassword)) {
                result = MESSAGE_PASSWORD_EXISTS ;
            }
            numberOfPasswords--;
        }
        br.close();
        return result;
    }
    private String passwordAlphanumericChecker(String newEnteredPassword){
        String result;
        if (newEnteredPassword.matches(".*\\d+.*") && newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
            return null;
        }
        else if (newEnteredPassword.matches(".*\\d+.*") && !newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
            result = String.format(MESSAGE_AT_LEAST_ONE, "alphabet");
        }
        else if (!newEnteredPassword.matches(".*\\d+.*") && newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
            result = String.format(MESSAGE_AT_LEAST_ONE, "number");
        }
        else {
            result = String.format(MESSAGE_AT_LEAST_ONE, "alphabet and at least one number.");
        }
        return result;
    }

    private String passwordLengthChecker(String newEnteredPassword){
        String result = null;
        int lengthPassword = newEnteredPassword.length();
        int minNumPassword = 5;
        if(lengthPassword < minNumPassword){
            result = String.format(MESSAGE_PASSWORD_LENGTH, lengthPassword)
                    + "\n" + String.format(MESSAGE_PASSWORD_MINIMUM_LENGTH, minNumPassword);
        }
        return result;
    }

    private String passwordValidityChecker(String newEnteredPassword) throws IOException {
        String result = null;
        if(passwordExistsChecker(newEnteredPassword) != null){
            result = passwordExistsChecker(newEnteredPassword);
        }
        else if(passwordAlphanumericChecker(newEnteredPassword) != null && passwordLengthChecker(newEnteredPassword) != null){
            result = passwordAlphanumericChecker(newEnteredPassword)
                    + "\n" + passwordLengthChecker(newEnteredPassword);
        }
        else if(passwordAlphanumericChecker(newEnteredPassword) != null ){
            result = passwordAlphanumericChecker(newEnteredPassword);
        }
        else if(passwordLengthChecker(newEnteredPassword) != null){
            result = passwordLengthChecker(newEnteredPassword);
        }
        return (result == null) ? null : result + MESSAGE_TRY_AGAIN ;
    }

    private String getUnauthorizedPOCommand(String input){
        String commandWord;
        if(isRejectPO(input)){
            commandWord = input;
        }
        else {
            input += " ";
            commandWord = input.substring(0,input.indexOf(" "));
        }
        return commandWord;
    }

    public boolean isUnauthorizedAccess(String input){
        return isPO() && invalidPOCommand(input);
    }

    private boolean invalidPOCommand(String input){
        String userCommandWord = getUnauthorizedPOCommand(input);
        return isRejectPO(userCommandWord);
    }

    private boolean isRejectPO(String userCommandWord){
        return (userCommandWord.equals("add") || userCommandWord.equals("delete") || userCommandWord.equals("clear")
                || userCommandWord.equals("edit") || userCommandWord.equals("check") || userCommandWord.equals("update password"));
    }

    public String invalidPOResult(String userCommandText) {
        return String.format(MESSAGE_TRY_UNAUTHORIZED, getUnauthorizedPOCommand(userCommandText));
    }

    public static String getID(){
        String result = null;
        if(isHQP){
//            result = MESSAGE_HQP;
            return "hqp";
        }
        else if(isPO1){
//            result = MESSAGE_PO + MESSAGE_ONE;
            return "po1";
        }
        else if(isPO2){
//            result = MESSAGE_PO + MESSAGE_TWO;
            return "po2";
        }
        else if(isPO3){
//            result = MESSAGE_PO + MESSAGE_THREE;
            return "po3";
        }
        else if(isPO4){
//            result = MESSAGE_PO + MESSAGE_FOUR;
            return "po4";
        }
        else if(isPO5){
//            result = MESSAGE_PO + MESSAGE_FIVE;
            return "po5";
        }
        return result;
    }
}
