package seedu.addressbook.commands;

public class LockCommand extends Command {
    public static final String COMMAND_WORD = "lock";
    public static final String MESSAGE_LOCK = "Your device has been locked." + "\n" + "Please enter password.\n";

    @Override
    public CommandResult execute() { return new CommandResult(MESSAGE_LOCK); }
}
