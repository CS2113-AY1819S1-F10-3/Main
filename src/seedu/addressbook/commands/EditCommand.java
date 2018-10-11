package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;
import seedu.addressbook.data.tag.Tag;

import java.util.HashSet;
import java.util.Set;

/**
 * Edits existing person in police records.
 */
public class EditCommand extends Command {
    private final Person afterEdited;

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Edits the person identified by the NRIC number.\n\t"
            + "Contact details can be marked private by prepending 'p' to the prefix.\n\t"
            + "Parameters: NAME [p]p/PHONE [p]e/EMAIL [p]a/ADDRESS  [t/TAG]...\n\t"
            + "Example: " + COMMAND_WORD
            + " John Doe p/987654321 e/johndoe@gmail.com a/311, Clementi Ave 3, #02-25 t/enemies t/paysOnTime";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the Police Records";

    // TODO: Matthew clean code, refactor edit and add command constructor
    public EditCommand(String name,
                       String phone, boolean isPhonePrivate,
                       String email, boolean isEmailPrivate,
                       String address, boolean isAddressPrivate,
                       Set<String> tags) throws IllegalValueException {

        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.afterEdited = new Person(
                new Name(name),
                new Phone(phone, isPhonePrivate),
                new Email(email, isEmailPrivate),
                new Address(address, isAddressPrivate),
                tagSet
        );

    }

    @Override
    public CommandResult execute() {
        try {
            final ReadOnlyPerson target = getTargetPerson(afterEdited.getName());
            addressBook.editPerson(target, afterEdited);
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, afterEdited.getName().toString()));
        } catch (UniquePersonList.PersonNotFoundException nfe) {
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        } catch (UniquePersonList.DuplicatePersonException dpe) {
            return new CommandResult(MESSAGE_DUPLICATE_PERSON);
        }
    }
}
