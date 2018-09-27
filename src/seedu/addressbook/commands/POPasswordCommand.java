package seedu.addressbook.commands;

public class POPasswordCommand extends Command {
    public static final String COMMAND_WORD = "password";
    public static final String MESSAGE_CORRECT_PASSWORD = "Welcome Police Officer.\n" +
            "You are not authorized to ADD, DELETE OR CLEAR.";

    @Override
    public CommandResult execute() {
        return new CommandResult(MESSAGE_CORRECT_PASSWORD);
    }
}
