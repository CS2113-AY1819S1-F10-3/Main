package seedu.addressbook.ui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import seedu.addressbook.Main;
import seedu.addressbook.commands.ExitCommand;
import seedu.addressbook.commands.LockCommand;
import seedu.addressbook.logic.Logic;
import seedu.addressbook.commands.CommandResult;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.autocorrect.EditDistance;
import seedu.addressbook.autocorrect.Dictionary;
import seedu.addressbook.password.Password;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
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


    public String checkDistance(String commandInput) {
        Dictionary A = new Dictionary();
        EditDistance B = new EditDistance();
        String prediction = "none";
        ArrayList<String> commandList = A.getCommands();
        int distance, check = 0;
        for (String command : commandList) {
            distance = B.computeDistance(commandInput, command);
            if (distance == 1) {
                prediction = command;
                check = 1;
                break;
            }
        }
        if (check == 0) {
            prediction = "none";
        }
        return prediction;
    }



    private static Timestamp currentDAT = new Timestamp(System.currentTimeMillis());
    private SimpleDateFormat timeStampFormatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
    private String outputDAT = timeStampFormatter.format(currentDAT);
    private String outputDATHrs = outputDAT + "hrs";

    private Password password = new Password();

    @FXML
    private TextArea outputConsole;

    @FXML
    private TextField commandInput;

    @FXML
    void onCommand(ActionEvent event) {
        try {
            String userCommandText = commandInput.getText();

            if(isExitCommand(userCommandText)){
                mainApp.stop();
            }
            else if(isLockCommand(userCommandText)){
                CommandResult result = logic.execute(userCommandText);
                clearScreen();
                displayResult(result);
            }
            else if(password.isLocked()) {
                String unlockDeviceResult = password.unlockDevice(userCommandText);
                clearScreen();
                display(unlockDeviceResult);
            }
            else if(canUpdatePassword(userCommandText)){
                String prepareUpdatePasswordResult = password.prepareUpdatePassword();
                clearScreen();
                display(prepareUpdatePasswordResult);
            }
            else if(password.isUpdatingPasswordNow()){
                String updatePasswordResult;
                if(password.isUpdatePasswordConfirmNow()) {
                    updatePasswordResult = password.updatePasswordFinal(userCommandText);
                }
                else{
                    updatePasswordResult = password.updatePassword(userCommandText);
                }
                clearScreen();
                display(updatePasswordResult);
            }

            else if(isUnauthorizedAccess(userCommandText)){
                clearScreen();
                display("You are unauthorized to " + userCommandText +".", "Please try a different command.");
                //TODO maybe change output message
            }
            else{
                String arr[] = userCommandText.split(" ", 2);
                String commandWordInput = arr[0];
                String output = checkDistance(commandWordInput);
                if(!(output.equals("none"))) {
                    clearCommandInput();
                    clearOutputConsole();
                    display("Did you mean to use " + output +"?", "Please try changing the command.");
                }
                else{
                    CommandResult result = logic.execute(userCommandText);
                    displayResult(result);
                    clearCommandInput();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            display(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void exitApp() throws Exception {
        mainApp.stop();
    }


    private boolean canUpdatePassword(String userCommandText){
        return password.isHQPUser() && userCommandText.equals("update password");
    }

    private boolean isUnauthorizedAccess(String userCommandText){
        return password.isPOUser() && isUnauthorizedPOCommand(userCommandText);
    }

    private boolean isUnauthorizedPOCommand(String input){
        return (input.contains("add") || input.contains("delete") || input.contains("clear") || input.contains("edit") || input.equals("update password"));
    }

    /** Returns true if the result given is the result of an exit command */
    private boolean isExitCommand(String userCommandText) {
        return userCommandText.equals(ExitCommand.COMMAND_WORD);
    }

    private boolean isLockCommand(String userCommandText) {
        return userCommandText.equals(LockCommand.COMMAND_WORD);
    }


    /** Clears the command input box */
    public void clearCommandInput() {
        commandInput.setText("");
    }

    /** Clears the output display area */
    public void clearOutputConsole(){
        outputConsole.clear();
    }

    public void clearScreen(){
        clearCommandInput();
        clearOutputConsole();
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
        display(MESSAGE_WELCOME, version, storageFileInfo, outputDATHrs + "\n" , password.MESSAGE_ENTER_PASSWORD);
    }

    /**
     * Displays the list of persons in the output display area, formatted as an indexed list.
     * Private contact details are hidden.
     */
    private void display(List<? extends ReadOnlyPerson> persons) {
        display(new Formatter().format(persons));
    }

    public void displayTimestamps(List<String> history){
        display(new Formatter().formatForTstamps(history));
    }

    /**
     * Displays the given messages on the output display area, after formatting appropriately.
     */
    public void display(String... messages) {
        outputConsole.setText(outputConsole.getText() + new Formatter().format(messages));
    }

}
