package seedu.addressbook.storage;

import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;
import seedu.addressbook.storage.jaxb.AdaptedAddressBook;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.*;


/**
 * Represents the file used to store address book data.
 */
public class StorageFile {

    /** Default file path used if the user doesn't provide the file name. */
    public static final String DEFAULT_STORAGE_FILEPATH = "policeRecords.txt";
    /* Note: Note the use of nested classes below.
     * More info https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html
     */

    /**
     * Signals that the given file path does not fulfill the storage filepath constraints.
     */
    public static class InvalidStorageFilePathException extends IllegalValueException {
        public InvalidStorageFilePathException(String message) {
            super(message);
        }
    }

    /**
     * Signals that some error has occured while trying to convert and read/write data between the application
     * and the storage file.
     */
    public static class StorageOperationException extends Exception {
        public StorageOperationException(String message) {
            super(message);
        }
    }

    private final JAXBContext jaxbContext;

    public final Path path;

    /**
     * @throws InvalidStorageFilePathException if the default path is invalid
     */
    public StorageFile() throws InvalidStorageFilePathException {
        this(DEFAULT_STORAGE_FILEPATH);
    }

    /**
     * @throws InvalidStorageFilePathException if the given file path is invalid
     */
    public StorageFile(String filePath) throws InvalidStorageFilePathException {
        try {
            jaxbContext = JAXBContext.newInstance(AdaptedAddressBook.class);
        } catch (JAXBException jaxbe) {
            throw new RuntimeException("jaxb initialisation error");
        }

        path = Paths.get(filePath);
        if (!isValidPath(path)) {
            throw new InvalidStorageFilePathException("Storage file should end with '.txt'");
        }
    }

    /**
     * Returns true if the given path is acceptable as a storage file.
     * The file path is considered acceptable if it ends with '.txt'
     */
    private static boolean isValidPath(Path filePath) {
        return filePath.toString().endsWith(".txt");
    }

    /**
     * Saves all data to this storage file.
     *
     * @throws StorageOperationException if there were errors converting and/or storing data to file.
     */
    public void save(AddressBook addressBook) throws StorageOperationException {

        /* Note: Note the 'try with resource' statement below.
         * More info: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
         */
        try (final Writer fileWriter =
                     new BufferedWriter(new FileWriter(path.toFile()))) {

            final AdaptedAddressBook toSave = new AdaptedAddressBook(addressBook);
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(toSave, fileWriter);

        } catch (IOException ioe) {
            throw new StorageOperationException("Error writing to file: " + path + " error: " + ioe.getMessage());
        } catch (JAXBException jaxbe) {
            throw new StorageOperationException("Error converting address book into storage format");
        }
    }

    /**
     * Loads data from this storage file.
     *
     * @throws StorageOperationException if there were errors reading and/or converting data from file.
     */
    public AddressBook load() throws StorageOperationException, IllegalValueException {
        try (final Reader fileReader =
                     new BufferedReader(new FileReader(path.toFile()))) {

            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            final AdaptedAddressBook loaded = (AdaptedAddressBook) unmarshaller.unmarshal(fileReader);
            // manual check for missing elements
            if (loaded.isAnyRequiredFieldMissing()) {
                throw new StorageOperationException("File data missing some elements");
            }
            return loaded.toModelType();

        /* Note: Here, we are using an exception to create the file if it is missing. However, we should minimize
         * using exceptions to facilitate normal paths of execution. If we consider the missing file as a 'normal'
         * situation (i.e. not truly exceptional) we should not use an exception to handle it.
         */

        // create empty file if not found
        } catch (FileNotFoundException fnfe) {

            //UniquePersonList data = populatePoliceRecords();

//            final AddressBook empty = new AddressBook();
//            save(empty);
//            return empty;

            //final AddressBook empty = new AddressBook();
            final AddressBook populated = populatedPoliceRecords();
            save(populated);
            return populated;


        // other errors
        } catch (IOException ioe) {
            throw new StorageOperationException("Error writing to file: " + path);
        } catch (JAXBException jaxbe) {
            throw new StorageOperationException("Error parsing file data format");
        } catch (IllegalValueException ive) {
            throw new StorageOperationException("File contains illegal data values; data type constraints not met");
        }
    }

    public String getPath() {
        return path.toString();
    }



//    public UniquePersonList populatePoliceRecords() throws IllegalValueException {
//        Person person1 = new Person(new Name("John Doe"), new NRIC("s1234567a"), new DateOfBirth("1990"), new PostalCode("111111"), new Status("xc"),
//                new Offense(), Collections.singleton(new Offense("riot")));
//        Person person2 = new Person(new Name("Doe John"), new NRIC("s7654321a"), new DateOfBirth("1976"), new PostalCode("222222"), new Status("xc"),
//                new Offense(), Collections.singleton(new Offense("theft")));
//        Person person3 = new Person(new Name("Syed Harith Zaki"), new NRIC("s9612485j"), new DateOfBirth("1996"), new PostalCode("666666"), new Status("wanted"),
//                new Offense(), Collections.singleton(new Offense("fleeing suspect")));
//        Person person4 = new Person(new Name("Mas Selamat"), new NRIC("g7511111p"), new DateOfBirth("1975"), new PostalCode("507709"), new Status("wanted"),
//                new Offense(), Collections.singleton(new Offense("fleeing suspect")));
//        return new UniquePersonList(person1,person2,person3,person4);
//    }


    //@@iamputradanish
    public AddressBook populatedPoliceRecords() throws IllegalValueException {
        return new AddressBook(new UniquePersonList(
                new Person(
                        new Name("John Doe"),
                        new NRIC("s1234567a"),
                        new DateOfBirth("1996"),
                        new PostalCode("510246"),
                        new Status("xc"),
                        new Offense("none"),
                        Collections.singleton(new Offense("riot"))),
                new Person(
                        new Name("Jane Doe"),
                        new NRIC("s9611234c"),
                        new DateOfBirth("1997"),
                        new PostalCode("510246"),
                        new Status("xc"),
                        new Offense("none"),
                        Collections.singleton(new Offense("riot")))));
    }

}
