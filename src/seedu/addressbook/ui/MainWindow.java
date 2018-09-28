package seedu.addressbook.ui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import seedu.addressbook.commands.ExitCommand;
import seedu.addressbook.commands.HQPPasswordCommand;
import seedu.addressbook.commands.POPasswordCommand;
import seedu.addressbook.commands.LockCommand;
import seedu.addressbook.commands.AddCommand;
import seedu.addressbook.commands.DeleteCommand;
import seedu.addressbook.commands.ClearCommand;
import seedu.addressbook.common.Messages;
import seedu.addressbook.logic.Logic;
import seedu.addressbook.commands.CommandResult;
import seedu.addressbook.data.person.ReadOnlyPerson;

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

    @FXML
    private TextArea outputConsole;

    @FXML
    private TextField commandInput;

    @FXML
    void onCommand(ActionEvent event) {
        try {
            String userCommandText = commandInput.getText();
            CommandResult result = logic.execute(userCommandText);
            if(isExitCommand(result)){
                exitApp();
            }
            else if(isLockCommand(result) && isUnlocked()){
                isHQP = false;
                isPO = false;
                wrongPasswordCounter = 6;
                clearOutputConsole();
                display(LockCommand.MESSAGE_LOCK);
                clearCommandInput();
            }
            else if(isHQP && !isPOPasswordCommand(result)){
                displayResult(result);
                clearCommandInput();
            }
            else if(isPO && !isHQPPasswordCommand(result)){
                if(isUnauthorizedPOCommand(result)){
                    clearOutputConsole();
                    display(MESSAGE_UNAUTHORIZED + userCommandText);
                    clearCommandInput();
                }
                else {
                    displayResult(result);
                    clearCommandInput();
                }
            }

            else if (isInvalidPasswordCommand(result)){
                displayResult(logic.execute("akshay")); //EDIT THIS
                clearCommandInput();
            }

            else if(isHQPPasswordCommand(result) && !isUnlocked()){
                isHQP = true;
                clearOutputConsole();
                display(HQPPasswordCommand.MESSAGE_CORRECT_PASSWORD);
                clearCommandInput();
            }
            else if(isPOPasswordCommand(result) && !isUnlocked()){
                isPO = true;
                clearOutputConsole();
                display(POPasswordCommand.MESSAGE_CORRECT_PASSWORD);
                clearCommandInput();
            }
            else if(!isUnlocked()){
                wrongPasswordCounter--;
                clearOutputConsole();
                display(MESSAGE_INCORRECT_PASSWORD,"You have " + wrongPasswordCounter + "" + " attempts left");
                clearCommandInput();
                if(wrongPasswordCounter==1) {
                    display("System will shut down if password is incorrect");
                }
                if(wrongPasswordCounter==0){
                    exitApp();
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

    private boolean isHQP = false;
    private boolean isPO = false;
    private int wrongPasswordCounter = 6;

    private boolean isUnlocked() {
        return (isPO || isHQP) ;
    }

    /** Returns true if the result given is the result of an exit command */
    private boolean isExitCommand(CommandResult result) {
        return result.feedbackToUser.equals(ExitCommand.MESSAGE_EXIT_ACKNOWEDGEMENT);
    }

    private boolean isHQPPasswordCommand(CommandResult result) {
        return result.feedbackToUser.equals(HQPPasswordCommand.MESSAGE_CORRECT_PASSWORD);
    }

    private boolean isPOPasswordCommand(CommandResult result) {
        return result.feedbackToUser.equals(POPasswordCommand.MESSAGE_CORRECT_PASSWORD);
    }

    private boolean isLockCommand(CommandResult result) {
        return result.feedbackToUser.equals(LockCommand.MESSAGE_LOCK);
    }

    private boolean isAddCommand(CommandResult result) {
        return (result.feedbackToUser.equals(AddCommand.MESSAGE_SUCCESS)
                || result.feedbackToUser.equals(AddCommand.MESSAGE_DUPLICATE_PERSON));
    }

    private boolean isDeleteCommand(CommandResult result) {
        return (result.feedbackToUser.equals(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS)
                || result.feedbackToUser.equals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX)
                || result.feedbackToUser.equals(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK));
    }

    private boolean isClearCommand(CommandResult result) {
        return result.feedbackToUser.equals(ClearCommand.MESSAGE_SUCCESS);
    }

    private boolean isUnauthorizedPOCommand(CommandResult result) {
        return (isAddCommand(result) || isClearCommand(result) || isDeleteCommand(result));
    }

    private boolean isInvalidPasswordCommand(CommandResult result) {
        return ((isHQP && isPOPasswordCommand(result)) || (isPO && isHQPPasswordCommand(result)));
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
        display(MESSAGE_WELCOME, version, MESSAGE_PROGRAM_LAUNCH_ARGS_USAGE, storageFileInfo, "\n", MESSAGE_ENTER_PASSWORD, "\n");
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
