package seedu.addressbook.ui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import seedu.addressbook.commands.ExitCommand;
import seedu.addressbook.commands.LockCommand;
import seedu.addressbook.logic.Logic;
import seedu.addressbook.commands.CommandResult;
import seedu.addressbook.data.person.ReadOnlyPerson;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static seedu.addressbook.common.Messages.*;

/**
 * Main Window of the GUI.
 */
public class MainWindow {

    private Logic logic;
    private Stoppable mainApp;

    public MainWindow(){
    }

    public void setLogic(Logic logic){
        this.logic = logic;
    }

    public void setMainApp(Stoppable mainApp){
        this.mainApp = mainApp;
    }

    private boolean isHQP = false;
    private boolean isPO = false;
    private boolean isLocked(){
        return !(isHQP || isPO);
    }
    private boolean isUpdatingPassword=false;
    private boolean isLogin(){
        return (isLoginHQP || isLoginPO);
    }
    private boolean isLoginHQP = false;
    private boolean isLoginPO = false;
    private boolean isPasswordMissingAlphabet = false;
    private boolean isPasswordMissingNumber = false;
    private boolean isOldPassword = false;
    private boolean isInvalidNewPassword(){
        return (isPasswordMissingAlphabet || isPasswordMissingNumber || isOldPassword);
    }
    private int wrongPasswordCounter=5;
    private boolean shutDown= false;
    private String loginEntered = null;

    private static Timestamp currentDAT = new Timestamp(System.currentTimeMillis());
    private SimpleDateFormat timeStampFormatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
    private String outputDAT = timeStampFormatter.format(currentDAT);
    private String outputDATHrs = outputDAT + "hrs";

    @FXML
    private TextArea outputConsole;

    @FXML
    private TextField commandInput;

    @FXML
    void onCommand(ActionEvent event) {
        try {
            String userCommandText = commandInput.getText();

            File originalFile = new File("passwordStorage.txt");
            BufferedReader br = new BufferedReader(new FileReader(originalFile));

            File tempFile = new File("tempfile.txt");
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            if(userCommandText.equals("exit")){
                mainApp.stop();
            }
            else if(userCommandText.equals("lock")){
                isHQP=false;
                isPO=false;
                wrongPasswordCounter=5;
                clearCommandInput();
                clearOutputConsole();
                display(LockCommand.MESSAGE_LOCK);
            }
            else if(isLocked()) {
                int hashedEnteredPassword= userCommandText.hashCode();

                String line = null;
                int numberOfPasswords = 2;

                while (numberOfPasswords > 0) {
                    line = br.readLine();
                    String storedCurrPassword = line.substring(line.lastIndexOf(" ") + 1, line.length());
                    String user = line.substring(0,line.lastIndexOf(" "));

                    if (user.equals("hqp") && storedCurrPassword.equals(Integer.toString(hashedEnteredPassword))) {
                        isHQP=true;
                        clearCommandInput();
                        clearOutputConsole();
                        display("Welcome Headquarters Personnel.", "Please enter a command: ");
                        break;
                    }
                    else if (user.equals("po") && storedCurrPassword.equals(Integer.toString(hashedEnteredPassword))) {
                        isPO=true;
                        clearCommandInput();
                        clearOutputConsole();
                        display("Welcome Police Officer.",
                                    "You are not authorized to ADD, DELETE, CLEAR nor EDIT.",
                                    "Please enter a command: ");
                        break;
                    }
                    numberOfPasswords--;
                }
                if(isLocked()){
                    wrongPasswordShutDown();
                }
                pw.close();
                br.close();
            }
            else if(isHQP && userCommandText.equals("update password")){
                clearCommandInput();
                clearOutputConsole();
                display("Please enter current password : ");
                isUpdatingPassword=true;
                wrongPasswordCounter=5;
            }
            else if(isUpdatingPassword){
                clearCommandInput();
                clearOutputConsole();

                String line = null;

                if(!isLogin()){
                    int enteredCurrentPassword = userCommandText.hashCode();

                    int numberOfPasswords =2;

                    while(numberOfPasswords>0) {
                        line = br.readLine();
                        String storedCurrPassword = line.substring(line.lastIndexOf(" ") + 1, line.length());
                        String user = line.substring(0,line.lastIndexOf(" "));

                        if(storedCurrPassword.equals(Integer.toString(enteredCurrentPassword))){
                            loginEntered=userCommandText;
                            wrongPasswordCounter=5;
                            if (user.equals("hqp")) {
                                isLoginHQP = true;
                                display("Enter New Alphanumeric Password for HQP: ");
                            }
                            else if(user.equals("po")){
                                isLoginPO = true;
                                display("Enter New Alphanumeric Password for PO: ");
                            }
                            break;
                        }
                        numberOfPasswords--;
                    }
                    pw.close();
                    br.close();

                    if(!isLogin()){
                        wrongPasswordShutDown();
                        if (shutDown){
                            mainApp.stop();
                        }
                    }
                }
                else{
                    passwordValidityChecker(userCommandText);
                    existingPassword(userCommandText);
                    if(!isInvalidNewPassword()) { //TODO problem updating password
                        int storedNewPassword = userCommandText.hashCode();
                        if (isLoginHQP) {
                            line = br.readLine();
                            line = line.substring(0, line.lastIndexOf(" ") + 1) + Integer.toString(storedNewPassword);
                            pw.println(line); //TODO loop this
                            pw.flush();
                            line = br.readLine();
                            pw.println(line);
                            pw.flush();
                            display("You have updated HQP password to : " + userCommandText);
                            isUpdatingPassword = false;
                            isLoginHQP = false;
                        } else if (isLoginPO) {
                            line = br.readLine(); //TODO loop this 2
                            pw.println(line);
                            pw.flush();
                            line = br.readLine();
                            line = line.substring(0, line.lastIndexOf(" ") + 1) + Integer.toString(storedNewPassword);
                            pw.println(line);
                            pw.flush();
                            display("You have updated PO password to : " + userCommandText);
                            isUpdatingPassword = false;
                            isLoginHQP = false;
                        }
                        pw.close();
                        br.close();

                        if (!originalFile.delete()) {
                            display("Could not delete file");
                            return;
                        }
                        if (!tempFile.renameTo(originalFile)) {
                            display("Could not rename file");
                        }

                    }
                }
            }

            else if(isPO && isUnauthorizedPOCommand(userCommandText)){
                clearCommandInput();
                clearOutputConsole();
                display("You are unauthorized to " + userCommandText +".", "Please try a different command.");
                //TODO maybe change output message
            }
            else{
                CommandResult result = logic.execute(userCommandText);
                displayResult(result);
                clearCommandInput();
            }
            pw.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            display(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void exitApp() throws Exception {
        mainApp.stop();
    }

    private void existingPassword (String newEnteredPassword){ //TODO password can be each other's password
        if(loginEntered.equals(newEnteredPassword)){
            isOldPassword=true;
            display("Your new password cannot be the same as your old password. Please try again.");
            display("Enter New Alphanumeric Password: ");
        }
    }

    private void passwordValidityChecker(String newEnteredPassword){
        if (newEnteredPassword.matches(".*\\d+.*") && newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
            isPasswordMissingNumber =false;
            isPasswordMissingAlphabet =false;
        }
        else if (newEnteredPassword.matches(".*\\d+.*") && !newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
            isPasswordMissingAlphabet =true;
            display("Your new password must contain at least one alphabet. Please try again.");
            display("Enter New Alphanumeric Password: ");
        } else if (!newEnteredPassword.matches(".*\\d+.*") && newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
            isPasswordMissingNumber = true;
            display("Your new password must contain at least one number. Please try again.");
            display("Enter New Alphanumeric Password: ");
        } else {
            isPasswordMissingNumber =true;
            isPasswordMissingAlphabet =true;
            display("Your new password can only be alphanumeric");
            display("Enter New Alphanumeric Password: ");
        }
    }
    private void wrongPasswordShutDown(){
        if(wrongPasswordCounter>1) {
            clearCommandInput();
            clearOutputConsole();
            display("Password is incorrect. Please try again.");
            display("You have " + wrongPasswordCounter + " attempts left. \n");
            display(MESSAGE_ENTER_PASSWORD);
            wrongPasswordCounter--;
        }
        else if(wrongPasswordCounter==1){
            clearCommandInput();
            clearOutputConsole();
            display("Password is incorrect. Please try again.");
            display("You have " + wrongPasswordCounter + " attempt left.");
            display("System will shut down if password is incorrect.");
            display(MESSAGE_ENTER_PASSWORD);
            wrongPasswordCounter--;
        }
        else if(wrongPasswordCounter==0){
            clearCommandInput();
            clearOutputConsole();
            display("Password is incorrect. System is shutting down.");
            shutDown=true;
        }
    }

    private boolean isUnauthorizedPOCommand(String input){
        return (input.contains("add ") || input.contains("delete") || input.contains("clear") || input.contains("edit") || input.equals("update password"));
    }

    /** Returns true if the result given is the result of an exit command */
    private boolean isExitCommand(CommandResult result) {
        return result.feedbackToUser.equals(ExitCommand.MESSAGE_EXIT_ACKNOWEDGEMENT);
    }

    private boolean isLockCommand(CommandResult result) {
        return result.feedbackToUser.equals(LockCommand.MESSAGE_LOCK);
    }

    /** Clears the command input box */
    private void clearCommandInput() {
        commandInput.setText("");
    }

    /** Clears the output display area */
    public void clearOutputConsole(){
        outputConsole.clear();
    }

    /** Displays the result of a command execution to the user. */
    public void displayResult(CommandResult result) {
        clearOutputConsole();
        final Optional<List<? extends ReadOnlyPerson>> resultPersons = result.getRelevantPersons();
        if(resultPersons.isPresent()) {
            display(resultPersons.get());
        }
        display(result.feedbackToUser);
    }

    public void displayWelcomeMessage(String version, String storageFilePath) {
        String storageFileInfo = String.format(MESSAGE_USING_STORAGE_FILE, storageFilePath);
        display(MESSAGE_WELCOME, version, storageFileInfo, outputDATHrs + "\n" , MESSAGE_ENTER_PASSWORD);
    }

    /**
     * Displays the list of persons in the output display area, formatted as an indexed list.
     * Private contact details are hidden.
     */
    private void display(List<? extends ReadOnlyPerson> persons) {
        display(new Formatter().format(persons));
    }

    /**
     * Displays the given messages on the output display area, after formatting appropriately.
     */
    private void display(String... messages) {
        outputConsole.setText(outputConsole.getText() + new Formatter().format(messages));
    }

}
