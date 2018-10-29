package seedu.addressbook.commands;

//@@author muhdharun
import org.javatuples.Triplet;
import seedu.addressbook.PatrolResourceStatus;
import seedu.addressbook.common.Location;

import java.util.ArrayList;
import java.util.List;

public class CheckPOStatusCommand extends Command {

    public static final String COMMAND_WORD = "checkstatus";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Gets current status of all POs \n\t"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        List<String> allPos = extractEngagementInformation(PatrolResourceStatus.getPatrolResourceStatus());

        return new CommandResult(getMessage(allPos));
    }

    public static List<String> extractEngagementInformation(ArrayList<Triplet<String, Location, Boolean>> pos) {
        List<String> allPos = new ArrayList<>();
        for (int i = 0 ; i < pos.size() ; i++){
            String poStatus = pos.get(i).getValue0() + " " + pos.get(i).getValue2();
            allPos.add(poStatus);
        }
        return allPos;
    }

}
