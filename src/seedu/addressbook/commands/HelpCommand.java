package seedu.addressbook.commands;


import seedu.addressbook.password.Password;

/**
 * Shows help instructions.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" +"Shows program usage instructions.\n\t"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_ALL_USAGES = AddCommand.MESSAGE_USAGE
            + "\n" + DeleteCommand.MESSAGE_USAGE
            + "\n" + ClearCommand.MESSAGE_USAGE
            + "\n" + EditCommand.MESSAGE_USAGE
            + "\n" + FindCommand.MESSAGE_USAGE
            + "\n" + ListCommand.MESSAGE_USAGE
            + "\n" + ViewAllCommand.MESSAGE_USAGE
            + "\n" + Password.UPDATE_PASSWORD_MESSAGE_USAGE
            + "\n" + HelpCommand.MESSAGE_USAGE
            + "\n" + LockCommand.MESSAGE_USAGE
            + "\n" + ExitCommand.MESSAGE_USAGE
            + "\n" + CheckCommand.MESSAGE_USAGE;

    public static final String MESSAGE_PO_USAGES = FindCommand.MESSAGE_USAGE
            + "\n" + ListCommand.MESSAGE_USAGE
            + "\n" + ViewAllCommand.MESSAGE_USAGE
            + "\n" + HelpCommand.MESSAGE_USAGE
            + "\n" + LockCommand.MESSAGE_USAGE
            + "\n" + ExitCommand.MESSAGE_USAGE;


    //@@author iamputradanish
    @Override
    public CommandResult execute() {
        Password password = new Password();
        boolean isHQPFlag = password.isHQPUser();

        if(isHQPFlag) {
            return new CommandResult(MESSAGE_ALL_USAGES);
        }
        else{
            return new CommandResult(MESSAGE_PO_USAGES);
        }
    }
}
