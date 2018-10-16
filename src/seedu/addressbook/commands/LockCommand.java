package seedu.addressbook.commands;

import seedu.addressbook.password.Password;

public class LockCommand extends Command {
    public static final String COMMAND_WORD = "lock";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Locks the device. No commands are allowed until correct password is entered.\n\t "
            + "Example: " + COMMAND_WORD ;
    public static final String MESSAGE_LOCK = "Your device has been locked." + "\n" + "Please enter password:\n";

    @Override
    public CommandResult execute() {
        Password password = new Password();
        password.setIsHQP(false);
        password.setIsPO(false);
        password.setWrongPasswordCounter(5);
        return new CommandResult(MESSAGE_LOCK); }
}
