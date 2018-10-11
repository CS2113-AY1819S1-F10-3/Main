package seedu.addressbook.ui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import seedu.addressbook.commands.ExitCommand;
import seedu.addressbook.commands.LockCommand;
import seedu.addressbook.commands.AddCommand;
import seedu.addressbook.commands.DeleteCommand;
import seedu.addressbook.commands.ClearCommand;
import seedu.addressbook.common.Messages;
import seedu.addressbook.logic.Logic;
import seedu.addressbook.commands.CommandResult;
import seedu.addressbook.data.person.ReadOnlyPerson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;

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

    public boolean isHQP = false;
    public boolean isPO = false;
    public boolean isLocked(){
        return !(isHQP || isPO);
    }
    public int wrongPasswordCounter=5;

    @FXML
    private TextArea outputConsole;

    @FXML
    private TextField commandInput;

    @FXML
    void onCommand(ActionEvent event) {
        try {
            String userCommandText = commandInput.getText();
            if(isLocked()) {
                File originalFile = new File("passwordStorage.txt");
                BufferedReader br = new BufferedReader(new FileReader(originalFile));

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
                        display("You have " + wrongPasswordCounter + " attempt left. \n");
                        display("System will shut down if password is incorrect.");
                        display(MESSAGE_ENTER_PASSWORD);
                        wrongPasswordCounter--;
                    }
                    else if(wrongPasswordCounter==0){
                        clearCommandInput();
                        clearOutputConsole();
                        display("Password is incorrect. System is shutting down.");
                        mainApp.stop();
                    }
                }

            }
            else if(isPO && isUnauthorizedPOCommand(userCommandText)){
                clearCommandInput();
                clearOutputConsole();
                display("You are unauthorized to " + userCommandText , "Please try a different command.");
                //TODO maybe change output message
            }
            else{
                CommandResult result = logic.execute(userCommandText);
                if (isExitCommand(result)) {
                    exitApp();
                }
                else if (isLockCommand(result)) {
                    isHQP=false;
                    isPO=false;
                    wrongPasswordCounter=5;
                    displayResult(result);
                    clearCommandInput();
                } else {
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

    private void exitApp() throws Exception {
        mainApp.stop();
    }

    private boolean isUnauthorizedPOCommand(String input){
        return (input.contains("add") || input.contains("delete") || input.contains("clear") || input.contains("edit"));
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
        display(MESSAGE_WELCOME, version, storageFileInfo + '\n', MESSAGE_ENTER_PASSWORD);
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
