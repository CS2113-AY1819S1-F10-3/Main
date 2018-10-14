package seedu.addressbook.storage.jaxb;

import seedu.addressbook.common.Utils;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;
import seedu.addressbook.data.tag.Tag;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.*;

/**
 * JAXB-friendly adapted person data holder class.
 */
public class AdaptedPerson {

    private static class AdaptedContactDetail {
        @XmlValue
        public String value;
        @XmlAttribute(required = true)
        public boolean isPrivate;
    }

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private AdaptedContactDetail nric;
    @XmlElement(required = true)
    private AdaptedContactDetail dateOfBirth;
    @XmlElement(required = true)
    private AdaptedContactDetail postalCode;
    @XmlElement(required = true)
    private AdaptedContactDetail status;
    @XmlElement(required = true)
    private AdaptedContactDetail wantedFor;

    @XmlElement
    private List<AdaptedTag> tagged = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public AdaptedPerson() {}


    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created AdaptedPerson
     */
    public AdaptedPerson(ReadOnlyPerson source) {
        name = source.getName().fullName;

        nric = new AdaptedContactDetail();
        nric.value = source.getNRIC().getIdentificationNumber();

        dateOfBirth = new AdaptedContactDetail();dateOfBirth = new AdaptedContactDetail();
        dateOfBirth.value = source.getDateOfBirth().getDOB();

        postalCode = new AdaptedContactDetail();
        postalCode.value = source.getPostalCode().getPostalCode();

        status = new AdaptedContactDetail();
        status.value = source.getStatus().getCurrentStatus();

        wantedFor = new AdaptedContactDetail();
        wantedFor.value = source.getWantedFor().getOffense();

        tagged = new ArrayList<>();
        for (Offense tag : source.getPastOffense()) {
            tagged.add(new AdaptedTag(tag));
        }

    }

    /**
     * Returns true if any required field is missing.
     *
     * JAXB does not enforce (required = true) without a given XML schema.
     * Since we do most of our validation using the data class constructors, the only extra logic we need
     * is to ensure that every xml element in the document is present. JAXB sets missing elements as null,
     * so we check for that.
     */
    public boolean isAnyRequiredFieldMissing() {
        for (AdaptedTag tag : tagged) {
            if (tag.isAnyRequiredFieldMissing()) {
                return true;
            }
        }
        // second call only happens if nric/postalCode/status are all not null
        return Utils.isAnyNull(name, nric, dateOfBirth, postalCode, status, wantedFor)
                || Utils.isAnyNull(nric.value, dateOfBirth.value, postalCode.value, status.value, wantedFor.value);
    }

    /**
     * Converts this jaxb-friendly adapted person object into the Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Person toModelType() throws IllegalValueException {
        final Set<Offense> tags = new HashSet<>();

        for (AdaptedTag tag : tagged) {
            tags.add(tag.toModelType());
        }
        Set<String> screeningHistory = new HashSet<>();

        final Name name = new Name(this.name);
        final NRIC nric = new NRIC(this.nric.value);
        final DateOfBirth dateOfBirth = new DateOfBirth(this.dateOfBirth.value);
        final PostalCode postalCode = new PostalCode(this.postalCode.value);
        final Status status = new Status(this.status.value);
        final Offense wantedFor = new Offense(this.wantedFor.value);
        return new Person(name, nric, dateOfBirth, postalCode, status, wantedFor, tags);
    }
}
