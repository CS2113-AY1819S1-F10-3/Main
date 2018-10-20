package seedu.addressbook.data;

import seedu.addressbook.data.person.NRIC;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList;
import seedu.addressbook.data.person.UniquePersonList.DuplicatePersonException;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;
import seedu.addressbook.readandwrite.ReaderAndWriter;
import seedu.addressbook.timeanddate.TimeAndDate;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents the entire address book. Contains the data of the address book.
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
        TimeAndDate timeAndDate = new TimeAndDate();
        tempNric = toAdd.getNric().getIdentificationNumber();
        tempTimestamp = timeAndDate.getOutputScreeningDAT();
    }

    public List<String> readDatabase(String nric) throws IOException {
        List<String> data = new ArrayList<>();
        String line;
        BufferedReader br = readerAndWriter.openReader(databaseFile);
        line = br.readLine();
        while (line != null){
            String[] parts = line.split(" ");
            if (parts[0].equals(nric)){
                data.add(parts[1]);
                line = br.readLine();
            }
            else{
                line = br.readLine();
            }
        }
        br.close();
        return data;
    }

    public void updateDatabase() throws IOException {
        String line;
        BufferedReader br = readerAndWriter.openReader(databaseFile);
        FileWriter write = new FileWriter(SCREENING_DATABASE,true);
        PrintWriter myPrinter = new PrintWriter(write);
        try {
            while ((line = br.readLine()) !=  null){
                String[] parts = line.split(" ");
                if (parts[0].equals(tempNric)){
                    myPrinter.println(tempNric + " " + tempTimestamp);
                    myPrinter.close();
                    br.close();
                    return;
                }
                line = br.readLine();
                continue;
            }
            myPrinter.println(tempNric + " " + tempTimestamp);
            myPrinter.close();
            br.close();
        }
        catch (Exception e){
            myPrinter.print(tempNric + " " + tempTimestamp);

            myPrinter.close();
            br.close();
        }
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
