package seedu.addressbook.data;

import seedu.addressbook.data.person.NRIC;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList;
import seedu.addressbook.data.person.UniquePersonList.DuplicatePersonException;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents the entire address book. Contains the data of the address book.
 */
public class AddressBook {

    public static final String SCREENING_DATABASE = "ScreeningHistory.txt";
    public static Timestamp screeningTimeStamp;
    private static final SimpleDateFormat timestampFormatter = new SimpleDateFormat("dd/MM/yyyy.HH:mm:ss");
    private String tempNric;
    private String tempTimestamp;

    private final UniquePersonList allPersons;

    public static AddressBook empty() {
        return new AddressBook();
    }

    /**
     * Creates an empty address book.
     */
    public AddressBook() {
        allPersons = new UniquePersonList();
    }

    /**
     * Constructs an address book with the given data.
     *
     * @param persons external changes to this will not affect this address book
     */
    public AddressBook(UniquePersonList persons) {
        this.allPersons = new UniquePersonList(persons);
    }

    /**
     * Adds a person to the address book.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(Person toAdd) throws UniquePersonList.DuplicateNricException {
        allPersons.add(toAdd);
    }

    public void addPersontoDbAndUpdate(ReadOnlyPerson toAdd) {
        tempNric = toAdd.getNRIC().getIdentificationNumber();
        screeningTimeStamp = new Timestamp(System.currentTimeMillis());
        tempTimestamp = timestampFormatter.format(screeningTimeStamp);
    }

    public List<String> readDatabase(String nric) throws IOException {
        List<String> data = new ArrayList<>();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(SCREENING_DATABASE));
        line = br.readLine();
        while (line != null){
            String[] parts = line.split(" ");
            if (parts[0].equals(nric)){
                data = new ArrayList<String>(Arrays.asList(parts));
                data.remove(0);
                break;
            }
        }
        return data;
    }

    public void updateDatabase() throws IOException {
        String line;
        BufferedReader br = new BufferedReader(new FileReader(SCREENING_DATABASE));
        FileWriter write = new FileWriter(SCREENING_DATABASE,true);
        PrintWriter myPrinter = new PrintWriter(write);
        line = br.readLine();
        if (line != null){
            while (line !=  null){
                String[] parts = line.split(" ");
                if (parts[0].equals(tempNric)){
                    String temp = line;
                    temp += " " + tempTimestamp + " ";
                    String finalLine = line.replaceAll(line,temp);
                    myPrinter.println("");
                    myPrinter.print(finalLine);
                }
                line = br.readLine();
                continue;
            }
        }
        else{
            myPrinter.println("");
            myPrinter.print(tempNric + " ");
            myPrinter.print(tempTimestamp + " ");

            myPrinter.close();
        }
        myPrinter.close();
        br.close();
    }

    /**
     * Checks if an equivalent person exists in the address book.
     */
    public boolean containsPerson(ReadOnlyPerson key) {
        return allPersons.contains(key);
    }


    /**
     * Removes the equivalent person from the address book.
     *
     * @throws PersonNotFoundException if no such Person could be found.
     */
    public void removePerson(ReadOnlyPerson toRemove) throws PersonNotFoundException {
        allPersons.remove(toRemove);
    }

    /**
     * Edits the equivalent person from the address book with new data fields.
     *
     * @throws PersonNotFoundException if no such Person could be found.
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
//    public void editPerson(ReadOnlyPerson toDelete, Person toAdd) throws PersonNotFoundException, DuplicatePersonException {
//        removePerson(toDelete);
//        addPerson(toAdd);
//    }

    /**
     * Clears all persons from the address book.
     */
    public void clear() {
        allPersons.clear();
    }

    /**
     * Defensively copied UniquePersonList of all persons in the address book at the time of the call.
     */
    public UniquePersonList getAllPersons() {
        return new UniquePersonList(allPersons);
    }

    public UniquePersonList getAllPersonsDirect() {
        return allPersons;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.allPersons.equals(((AddressBook) other).allPersons));
    }

    @Override
    public int hashCode() {
        return allPersons.hashCode();
    }
}
