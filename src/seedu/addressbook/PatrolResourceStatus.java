package seedu.addressbook;

import org.javatuples.Pair;
import seedu.addressbook.common.Location;
import seedu.addressbook.data.PoliceOfficers.PatrolResource;

import java.util.ArrayList;

public class PatrolResourceStatus {
    private static ArrayList<Pair<String, Location>> patrolResourceStatus = new ArrayList<>(){{
        add(new Pair<>("hqp", new Location(1.294166, 103.770730))); // NUS FASS
        add(new Pair<>("po1", new Location(1.306935, 103.790564))); // Buona Vista
        add(new Pair<>("po2", new Location(1.346301, 103.682060))); // NTU
        add(new Pair<>("po3", new Location(1.296057, 103.849865))); // SMU
        add(new Pair<>("po4", new Location(1.340352, 103.962193))); // SUTD
        add(new Pair<>("po5", new Location(1.329393, 103.776169))); // SIM
    }};

    public PatrolResourceStatus() {
    }


    public static Location getLocation(String patrolResource) {
        switch (patrolResource) {
            case "hqp":
                return patrolResourceStatus.get(0).getValue1();
            case "po1":
                return patrolResourceStatus.get(1).getValue1();
            case "po2":
                return patrolResourceStatus.get(2).getValue1();
            case "po3":
                return patrolResourceStatus.get(3).getValue1();
            case "po4":
                return patrolResourceStatus.get(4).getValue1();
            case "po5":
                return patrolResourceStatus.get(5).getValue1();
            default:
                return null;
        }
    }
}
