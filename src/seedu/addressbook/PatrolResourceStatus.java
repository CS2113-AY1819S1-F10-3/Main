//@@author andyrobert3
package seedu.addressbook;

import org.javatuples.Triplet;
import seedu.addressbook.common.Location;

import java.util.ArrayList;

public class PatrolResourceStatus {
    // Triplet<Police Officer ID, Location, isEngaged
    private static ArrayList<Triplet<String, Location, Boolean>> patrolResourceStatus = new ArrayList<>(){{
        add( new Triplet<>("hqp", new Location(1.294166, 103.770730), false) ); // NUS FASS
        add( new Triplet<>("po1", new Location(1.306935, 103.790564), false) ); // Buona Vista
        add( new Triplet<>("po2", new Location(1.346301, 103.682060), false) ); // NTU
        add( new Triplet<>("po3", new Location(1.296057, 103.849865), false) ); // SMU
        add( new Triplet<>("po4", new Location(1.340352, 103.962193), false) ); // SUTD
        add( new Triplet<>("po5", new Location(1.329393, 103.776169), false) ); // SIM
    }};

    public static ArrayList<Triplet<String, Location, Boolean>> getPatrolResourceStatus() {
        return patrolResourceStatus;
    }

    // TODO: put into message
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
                return patrolResourceStatus.get(0).getValue1(); // default for test cases
        }
    }

    public static void setStatus(String policeOfficerId, Boolean status) {
        for (Triplet<String, Location, Boolean> policeOfficer : patrolResourceStatus) {
            if (policeOfficer.getValue0().equalsIgnoreCase(policeOfficerId)) {
                policeOfficer.setAt2(status);
                return;
            }
        }
    }

}
