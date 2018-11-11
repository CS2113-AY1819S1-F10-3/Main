//@@author ShreyasKp
package seedu.addressbook.commands;

import seedu.addressbook.timeanddate.TimeAndDate;

/**
 * Command to display current date and time on screen
 */

public class DateTimeCommand extends Command {

    public static final String COMMAND_WORD = "time";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Returns current date and time \n\t"
            + "Example: " + COMMAND_WORD;

    private TimeAndDate timeAndDate;

    public DateTimeCommand() {
        timeAndDate = new TimeAndDate();
    }

    @Override
    public CommandResult execute() {
        return new CommandResult(timeAndDate.outputDatMainHrs());
    }
}
