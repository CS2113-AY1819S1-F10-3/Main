package seedu.addressbook.data;

import seedu.addressbook.data.person.NRIC;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList;
import seedu.addressbook.data.person.UniquePersonList.DuplicatePersonException;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;
import seedu.addressbook.password.Password;
import seedu.addressbook.readandwrite.ReaderAndWriter;
import seedu.addressbook.timeanddate.TimeAndDate;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents PRISM. Contains the data of PRISM.
 */
public class AddressBook {

    private static final String SCREENING_DATABASE = "ScreeningHistory.txt";
    private String tempNric;
    private String tempTimestamp;
    private int counter = 0;

    private final UniquePersonList allPersons;
    private ReaderAndWriter readerAndWriter = new ReaderAndWriter();
    private File databaseFile = readerAndWriter.fileToUse(SCREENING_DATABASE);

    public static AddressBook empty() {
        return new AddressBook();
    }

    /**
     * Creates an empty PRISM system.
     */
    public AddressBook() {
        allPersons = new UniquePersonList();
    }

    /**
     * Constructs a PRISM system with the given data.
     *
     * @param persons external changes to this will not affect PRISM
     */
    public AddressBook(UniquePersonList persons) {
        this.allPersons = new UniquePersonList(persons);
    }

    /**
     * Adds a person to PRISM.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(Person toAdd) throws UniquePersonList.DuplicateNricException {
        allPersons.add(toAdd);
    }
//@@author muhdharun

    /**
     * Sets the timestamp of screening, as well as the NRIC to be added
     */
    public void addPersonToDbAndUpdate(ReadOnlyPerson toAdd) {
        TimeAndDate timeAndDate = new TimeAndDate();
        tempNric = toAdd.getNric().getIdentificationNumber();
        tempTimestamp = timeAndDate.outputDATHrs();
    }

    /**
     * Reads the txt file to get the timestamps for the specified NRIC
     */

    public List<String> readDatabase(String nric) throws IOException {
        List<String> data = new ArrayList<>();
        String line;
        BufferedReader br = readerAndWriter.openReader(databaseFile);
        line = br.readLine();
        while (line != null){
            String[] parts = line.split(" ");

            if (parts[0].equals(nric)){
                if(parts[2].equals("null")){
                    continue;
                }
                data.add(parts[1] + " by " + parts[2]);
                line = br.readLine();
            }
            else{
                line = br.readLine();
            }
        }
        br.close();
        return data;
    }

    /**
     * Adds the timestamp, the respective NRIC and the PO who screened the person (using 'find' command)
     */

    public void updateDatabase() throws IOException {
        String line;
        BufferedReader br = readerAndWriter.openReader(databaseFile);
        FileWriter write = new FileWriter(SCREENING_DATABASE,true);
        PrintWriter myPrinter = new PrintWriter(write);
        try {
            while ((line = br.readLine()) !=  null){
                String[] parts = line.split(" ");
                if (parts[0].equals(tempNric)){
                    myPrinter.println(tempNric + " " + tempTimestamp + " " + Password.getID());
                    myPrinter.close();
                    br.close();
                    return;
                }
                line = br.readLine();
                continue;
            }
            myPrinter.println(tempNric + " " + tempTimestamp + " " + Password.getID());
            myPrinter.close();
            br.close();
        }
        catch (Exception e){
            myPrinter.print(tempNric + " " + tempTimestamp + " " + Password.getID());

            myPrinter.close();
            br.close();
        }
    }
//@@author
    /**
     * Checks if an equivalent person exists in PRISM.
     */
    public boolean containsPerson(ReadOnlyPerson key) {
        return allPersons.contains(key);
    }


    /**
     * Removes the equivalent person from PRISM.
     *
     * @throws PersonNotFoundException if no such Person could be found.
     */
    public void removePerson(ReadOnlyPerson toRemove) throws PersonNotFoundException {
        allPersons.remove(toRemove);
    }

    /**
     * Edits the equivalent person from PRISM with new data fields.
     *
     * @throws PersonNotFoundException if no such Person could be found.
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
//    public void editPerson(ReadOnlyPerson toDelete, Person toAdd) throws PersonNotFoundException, DuplicatePersonException {
//        removePerson(toDelete);
//        addPerson(toAdd);
//    }

    /**
     * Clears all persons from PRISM.
     */
    public void clear() {
        allPersons.clear();
    }

    /**
     * Defensively copied UniquePersonList of all persons in PRISM at the time of the call.
     */
    public UniquePersonList getAllPersons() {
        return new UniquePersonList(allPersons);
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
