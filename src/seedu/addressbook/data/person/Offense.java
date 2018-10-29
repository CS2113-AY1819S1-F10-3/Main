package seedu.addressbook.data.person;
//@@author muhdharun
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.inbox.Msg;

import java.util.*;

public class Offense {
    public static final String EXAMPLE = "theft";
    public static final String NULL_OFFENSE = "none";
    public static final String MESSAGE_OFFENSE_INVALID = "Offense must be inside the list:\n";

//@@author andyrobert3
    private static HashMap<String, Msg.Priority> OFFENSE_LIST = new HashMap<>();
    static {
        OFFENSE_LIST.put("none", Msg.Priority.LOW);
        OFFENSE_LIST.put("cheating", Msg.Priority.LOW);
        OFFENSE_LIST.put("piracy", Msg.Priority.LOW);
        OFFENSE_LIST.put("dispute", Msg.Priority.LOW);
        OFFENSE_LIST.put("unsound", Msg.Priority.LOW);
        OFFENSE_LIST.put("theft", Msg.Priority.MED);
        OFFENSE_LIST.put("accident", Msg.Priority.MED);
        OFFENSE_LIST.put("abetment", Msg.Priority.MED);
        OFFENSE_LIST.put("fire", Msg.Priority.MED);
        OFFENSE_LIST.put("outrage-of-modesty", Msg.Priority.MED);
        OFFENSE_LIST.put("murder", Msg.Priority.HIGH);
        OFFENSE_LIST.put("riot", Msg.Priority.HIGH);
        OFFENSE_LIST.put("murder", Msg.Priority.MED);
        OFFENSE_LIST.put("suspect-loose", Msg.Priority.MED);
        OFFENSE_LIST.put("gun", Msg.Priority.HIGH);
        OFFENSE_LIST.put("gunman", Msg.Priority.HIGH);
        OFFENSE_LIST.put("fleeing suspect", Msg.Priority.HIGH);
        OFFENSE_LIST.put("assault", Msg.Priority.HIGH);
        OFFENSE_LIST.put("attempted-suicide", Msg.Priority.HIGH);
        OFFENSE_LIST.put("drugs", Msg.Priority.HIGH);
        OFFENSE_LIST.put("homicide", Msg.Priority.HIGH);
        OFFENSE_LIST.put("hostage", Msg.Priority.HIGH);
        OFFENSE_LIST.put("house-break", Msg.Priority.HIGH);
        OFFENSE_LIST.put("kidnap", Msg.Priority.HIGH);
        OFFENSE_LIST.put("manslaughter", Msg.Priority.HIGH);
        OFFENSE_LIST.put("rape", Msg.Priority.HIGH);
        OFFENSE_LIST.put("robbery", Msg.Priority.HIGH);
        OFFENSE_LIST.put("wanted", Msg.Priority.HIGH);
        OFFENSE_LIST.put("theft1", Msg.Priority.MED);
        OFFENSE_LIST.put("theft2", Msg.Priority.MED);
        OFFENSE_LIST.put("theft3", Msg.Priority.MED);
        OFFENSE_LIST.put("theft4", Msg.Priority.MED);

    }
//@@author muhdharun
    private final String offense;

    public static String getListOfValidOffences() {
        String result = "";
        for (HashMap.Entry<String,Msg.Priority> entry : OFFENSE_LIST.entrySet()) {
            if (!entry.getKey().matches(".*\\d+.*")) {
                result += entry.getKey() + "\n";
            }
        }
        return result + "";
    }

    public Offense(){
        this.offense = "none";
    }

    /**
     * Validates given tag name.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Offense(String offense) throws IllegalValueException {
        offense = offense.toLowerCase().trim();

        if (!isValidOffense(offense)) {
            throw new IllegalValueException(MESSAGE_OFFENSE_INVALID + "\n" + getListOfValidOffences());
        }

        this.offense = offense;
    }

//    private static String getOffenseList() {
//        String offenseList = "";
//        for (Map.Entry<String, Msg.Priority> entry : OFFENSE_LIST.entrySet()) {
//            offenseList += entry.getKey() + "\n";
//        }
//        return offenseList;
//    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidOffense(String offense) {
        return OFFENSE_LIST.containsKey(offense.toLowerCase());
    }

    public String getOffense() {
        return offense;
    }
//@@author andyrobert3
    /**
     * Returns priority for a given offense.
     *
     * @params offense name
     * @return priority
     */
    public static Msg.Priority getPriority(String offense) throws IllegalValueException {
        offense = offense.toLowerCase();
        if (!OFFENSE_LIST.containsKey(offense)) {
            throw new IllegalValueException("Offense does not exist in database");
        }

        return OFFENSE_LIST.get(offense);
    }

    public static int getPriority(Msg.Priority priority) {
        return priority.toInteger();
    }
//@@author muhdharun
    public static Set<Offense> getOffenseSet(Set<String> offenseStringSet) throws IllegalValueException {
        Set<Offense> offenseSet = new HashSet<>();

        for (String offense: offenseStringSet) {
            offenseSet.add(new Offense(offense));
        }

        return offenseSet;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Offense // instanceof handles nulls
                && this.offense.equals(((Offense) other).offense)); // state check
    }

    @Override
    public int hashCode() {
        return offense.hashCode();
    }

    @Override
    public String toString() {
        return '[' + offense + ']';
    }
}
