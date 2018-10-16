package seedu.addressbook.password;

import seedu.addressbook.common.Messages;
import seedu.addressbook.readandwrite.ReaderAndWriter;
import seedu.addressbook.ui.MainWindow;

import java.io.*;


public class Password {

    private static int wrongPasswordCounter=5;
    private static String formatWrongPasswordCounter = Integer.toString(wrongPasswordCounter);

    public static final String MESSAGE_ENTER_PASSWORD = "Please enter password: ";
    private static final String MESSAGE_ENTER_COMMAND = "Please enter a command: ";
    private static final String MESSAGE_WELCOME = "Welcome ";
    private static final String MESSAGE_UNAUTHORIZED = "You are not authorized to ADD, CLEAR, DELETE, EDIT nor UPDATE PASSWORD.";
    private static final String MESSAGE_INCORRECT_PASSWORD = "Password is incorrect. Please try again.";
    private static final String MESSAGE_ATTEMPTS_LEFT = "You have "
                        + formatWrongPasswordCounter
                        + " attempts left.";
    private static final String MESSAGE_ATTEMPT_LEFT = "You have "
                        + formatWrongPasswordCounter
                        + " attempt left.";
    private static final String MESSAGE_SHUTDOWN_WARNING = "System will shut down if password is incorrect.";
    private static final String MESSAGE_SHUTDOWN = "Password is incorrect. System is shutting down.";
    private static final String MESSAGE_ENTER_PASSWORD_TO_CHANGE = "Please enter current password to change: ";
    private static final String MESSAGE_HQP = "Headquarter Personnel";
    private static final String MESSAGE_PO = "Police Officer";
    private static final String MESSAGE_ONE = " Oscar November Echo.";
    private static final String MESSAGE_TWO = " Tango Whiskey Oscar.";
    private static final String MESSAGE_THREE = " Tango Hotel Romeo Echo Echo.";
    private static final String MESSAGE_FOUR = " Foxtrot Oscar Uniform Romeo.";
    private static final String MESSAGE_FIVE = " Foxtrot India Victor Echo.";
    private static final String MESSAGE_ENTER_NEW_PASSWORD = "Enter New Alphanumeric Password for ";
    private static final String MESSAGE_ENTER_NEW_PASSWORD_AGAIN = "Please New Alphanumeric Password again: ";


    private static boolean isHQP = false;
    private static boolean isPO = false;
    public static void setIsHQP(boolean isHQP) {
        Password.isHQP = isHQP;
    }
    public static void setIsPO(boolean isPO) {
        Password.isPO = isPO;
    }
    public boolean isHQPUser() {
        return isHQP;
    }
    public static boolean isPOUser() {
        return isPO;
    }
    public boolean isLocked(){
        return !(isHQP || isPO);
    }

    public boolean isUpdatingPasswordNow() {
        return isUpdatingPassword;
    }

    private boolean isUpdatingPassword = false;
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

    private boolean isPasswordMissingAlphabet = false;
    private boolean isPasswordMissingNumber = false;
    private boolean isOldPassword = false;
    private boolean isValidNewPassword(){
        return (!isPasswordMissingAlphabet && !isPasswordMissingNumber && !isOldPassword);
    }

    public void setWrongPasswordCounter(int wrongPasswordCounter) {
        Password.wrongPasswordCounter = wrongPasswordCounter;
    }

    public boolean isUpdatePasswordConfirmNow() {
        return isUpdatePasswordConfirm;
    }

    private boolean isUpdatePasswordConfirm = false;

    private boolean shutDown= false;
    private String loginEntered = null;
    private String oneTimePassword = null;


    private MainWindow mainwindow = new MainWindow();
    private ReaderAndWriter readerandwriter = new ReaderAndWriter();

    public String unlockDevice(String userCommandText) throws IOException {

        String result = null;

        File originalFile =readerandwriter.fileToUse("passwordStorage.txt");
        BufferedReader br = readerandwriter.openReader(originalFile);

        int hashedEnteredPassword= userCommandText.hashCode();

        String line;
        int numberOfPasswords = 6;

        while (numberOfPasswords > 0) {
            line = br.readLine();
            String storedCurrPassword = line.substring(line.lastIndexOf(" ") + 1, line.length());
            String user = line.substring(0,line.lastIndexOf(" "));

            if (correctHQP(user,storedCurrPassword,hashedEnteredPassword)) {
                isHQP = true;
                result = MESSAGE_WELCOME + MESSAGE_HQP + ".\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else if (correctPO1(user,storedCurrPassword,hashedEnteredPassword)) {
                isPO = true;
                result = MESSAGE_WELCOME + MESSAGE_PO + MESSAGE_ONE + "\n"
                        + MESSAGE_UNAUTHORIZED + "\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else if (correctPO2(user,storedCurrPassword,hashedEnteredPassword)) {
                isPO = true;
                result = MESSAGE_WELCOME + MESSAGE_PO + MESSAGE_TWO + "\n"
                        + MESSAGE_UNAUTHORIZED + "\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else if (correctPO3(user,storedCurrPassword,hashedEnteredPassword)) {
                isPO = true;
                result = MESSAGE_WELCOME + MESSAGE_PO + MESSAGE_THREE + "\n"
                        + MESSAGE_UNAUTHORIZED + "\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else if (correctPO4(user,storedCurrPassword,hashedEnteredPassword)) {
                isPO = true;
                result = MESSAGE_WELCOME + MESSAGE_PO + MESSAGE_FOUR + "\n"
                        + MESSAGE_UNAUTHORIZED + "\n"
                        + MESSAGE_ENTER_COMMAND;
                break;
            }
            else if (correctPO5(user,storedCurrPassword,hashedEnteredPassword)) {
                isPO = true;
                result = MESSAGE_WELCOME + MESSAGE_PO + MESSAGE_FIVE + "\n"
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
            result = wrongPasswordShutDown();
        }
        br.close();
        return result;
    }

    private String wrongPasswordShutDown(){
        String result;
        if(wrongPasswordCounter>1) {
            result = MESSAGE_INCORRECT_PASSWORD
                    + "\n" + MESSAGE_ATTEMPTS_LEFT
                    + "\n" + MESSAGE_ENTER_PASSWORD;
            decreaseWrongPasswordCounter();
        }
        else if(wrongPasswordCounter==1){
            result = MESSAGE_INCORRECT_PASSWORD
                    + "\n" + MESSAGE_ATTEMPT_LEFT
                    + "\n" + MESSAGE_SHUTDOWN_WARNING;
            decreaseWrongPasswordCounter();
        }
        else if(wrongPasswordCounter==0){
            result = MESSAGE_SHUTDOWN;
            shutDown=true;
        }
        else{
            result = Messages.MESSAGE_ERROR;
        }
        return result;
    }

    private void decreaseWrongPasswordCounter(){
        wrongPasswordCounter--;
    }

    private boolean correctPassword(String storedCurrPassword , int hashedEnteredPassword){
        return storedCurrPassword.equals(Integer.toString(hashedEnteredPassword));
    }

    private boolean correctHQP(String user , String storedCurrPassword , int hashedEnteredPassword){
        return user.equals("hqp") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }

    private boolean correctPO1(String user , String storedCurrPassword , int hashedEnteredPassword){
        return user.equals("po1") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }
    private boolean correctPO2(String user , String storedCurrPassword , int hashedEnteredPassword){
        return user.equals("po2") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }
    private boolean correctPO3(String user , String storedCurrPassword , int hashedEnteredPassword){
        return user.equals("po3") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }
    private boolean correctPO4(String user , String storedCurrPassword , int hashedEnteredPassword){
        return user.equals("po4") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }
    private boolean correctPO5(String user , String storedCurrPassword , int hashedEnteredPassword){
        return user.equals("po5") && correctPassword(storedCurrPassword, hashedEnteredPassword);
    }

    public String prepareUpdatePassword(){
        isUpdatingPassword = true;
        wrongPasswordCounter = 5;
        return MESSAGE_ENTER_PASSWORD_TO_CHANGE;
    }

    public String updatePassword(String userCommandText) throws Exception {

        String result= null;

        File originalFile =readerandwriter.fileToUse("passwordStorage.txt");
        BufferedReader br = readerandwriter.openReader(originalFile);

        String line;

        if(isNotLogin()){

            int enteredCurrentPassword = userCommandText.hashCode();

            int numberOfPasswords = 6;

            while(numberOfPasswords>0) {
                line = br.readLine();
                String storedCurrPassword = line.substring(line.lastIndexOf(" ") + 1, line.length());
                String user = line.substring(0,line.lastIndexOf(" "));

                if(correctPassword(storedCurrPassword, enteredCurrentPassword)){
                    loginEntered = userCommandText;
                    wrongPasswordCounter=5;

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
                wrongPasswordShutDown();
                if (shutDown){
                    mainwindow.exitApp();
                }
            }
        }
        else{
            passwordValidityChecker(userCommandText); //TODO remove mainWindow calls
            existingPassword(userCommandText);
            if(isValidNewPassword()){
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
        int lineNumber = 0 ;

        File originalFile = readerandwriter.fileToUse("passwordStorage.txt");
        BufferedReader br = readerandwriter.openReader(originalFile);
        File tempFile = readerandwriter.fileToUse("tempFile.txt");
        PrintWriter pw = readerandwriter.openTempWriter(tempFile);

        String line = null;

        if(isEqualPassword(userCommandText)) {
            int storedNewPassword = userCommandText.hashCode();

            if(isLoginHQP){
                isLoginHQP = false;
                result = "You have updated HQP password to : " + userCommandText; //TODO create messages
            }
            else if(isLoginPO1){
                lineNumber = 1;
                isLoginPO1 = false;
                result = "You have updated PO1 password to : " + userCommandText;
            }
            else if(isLoginPO2){
                lineNumber = 2;
                isLoginPO2 = false;
                result = "You have updated PO2 password to : " + userCommandText;
            }
            else if(isLoginPO3){
                lineNumber = 3;
                isLoginPO3 = false;
                result = "You have updated PO3 password to : " + userCommandText;
            }
            else if(isLoginPO4){
                lineNumber = 4;
                isLoginPO4 = false;
                result = "You have updated PO4 password to : " + userCommandText;
            }
            else if(isLoginPO5){
                lineNumber = 5;
                isLoginPO5 = false;
                result = "You have updated PO5 password to : " + userCommandText;
            }
            isUpdatingPassword = false;
            isUpdatePasswordConfirm = false;

            while (lineNumber > 0){
                line = br.readLine();
                pw.println(line);
                pw.flush();
                lineNumber--;
            }
            line = line.substring(0, line.lastIndexOf(" ") + 1) + Integer.toString(storedNewPassword);
            pw.println(line);
            pw.flush();
            pw.close();
            br.close();

            if (!originalFile.delete()) {
                result = "Could not delete file";
            }
            if (!tempFile.renameTo(originalFile)) {
                result = "Could not rename file";
            }

        }
        else{
            isUpdatePasswordConfirm = false;
            result = "The password you entered is not the same. Please try again.";
        }
        pw.close();
        br.close();
        return result;
    }

    private void existingPassword (String newEnteredPassword){ //TODO password can be each other's password
        if(loginEntered.equals(newEnteredPassword)){
            isOldPassword = true;
            mainwindow.display("Your new password cannot be the same as your old password. Please try again.");
            mainwindow.display("Enter New Alphanumeric Password: ");
        }
    }

    private void passwordValidityChecker(String newEnteredPassword){
        if (newEnteredPassword.matches(".*\\d+.*") && newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
            isPasswordMissingNumber = false;
            isPasswordMissingAlphabet = false;
        }
        else if (newEnteredPassword.matches(".*\\d+.*") && !newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
            isPasswordMissingAlphabet = true;
            mainwindow.display("Your new password must contain at least one alphabet. Please try again.");
            mainwindow.display("Enter New Alphanumeric Password: ");
        } else if (!newEnteredPassword.matches(".*\\d+.*") && newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
            isPasswordMissingNumber = true;
            mainwindow.display("Your new password must contain at least one number. Please try again.");
            mainwindow.display("Enter New Alphanumeric Password: ");
        } else {
            isPasswordMissingNumber = true;
            isPasswordMissingAlphabet = true;
            mainwindow.display("Your new password can only be alphanumeric");
            mainwindow.display("Enter New Alphanumeric Password: ");
        }
    }

}
