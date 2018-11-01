//@@author ShreyasKp
package seedu.addressbook.autocorrect;

import seedu.addressbook.commands.*;
import seedu.addressbook.password.Password;

import static seedu.addressbook.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class AutoCorrect {

    CheckDistance checker = new CheckDistance();

    private Password password = new Password();

    public String checkCommand(String commandInput) {
        String output = checker.checkDistance(commandInput);
        String displayCommand = "not working";
        if(!(output.equals("none"))) {
            switch (output) {
                case AddCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case DateTimeCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DateTimeCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case DeleteCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case EditCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case ClearCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case FindCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case ListCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case ViewAllCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewAllCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case ExitCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExitCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case LockCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LockCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case DispatchCommand.COMMAND_WORD:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DispatchCommand.MESSAGE_USAGE)).feedbackToUser;
                    break;

                case HelpCommand.COMMAND_WORD: // Fallthrough
                default:
                    displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE)).feedbackToUser;
            }
            int i = displayCommand.indexOf("!");
            displayCommand = displayCommand.substring(i + 1);
        }
        else {
            boolean isHQPFlag = password.isHQPUser();
            if(isHQPFlag) {
                displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_ALL_USAGES)).feedbackToUser;
            }
            else {
                displayCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_PO_USAGES)).feedbackToUser;
            }
        }
        return displayCommand;
    }
}
