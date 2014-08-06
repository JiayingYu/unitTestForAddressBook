package addressBook;

import java.util.Comparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents an entry that can be stored in an {@link AddressBook}
 * 
 * @author ck1456@nyu.edu
 * 
 * @see AddressBook
 */
public final class Contact {

  private ContactName name;

  /**
   * Gets the name associated with this contact. All contacts are required to
   * have a name so this will never return null.
   * 
   * @return the name field associated with this contact
   */
  public ContactName getName() {
    return name;
  }

  /**
   * Sets a new ContactName for this contact. All contacts are required to have
   * a non-null name.
   * 
   * @param name
   *          the ContactName for this contact (cannot be null)
   */
  public void setName(ContactName name) {
    if (name == null) {
      throw new IllegalArgumentException(
          "A Contact is required to have a non-null ContactName");
    }
    this.name = name;
  }

  private String emailAddress;

  /**
   * Gets the email address associated with this contact.
   * 
   * @return (possibly null) email address.
   */
  public String getEmailAddress() {
    return emailAddress;
  }

  /**
   * Updates the email address for this contact. Email addresses are not
   * validated in this implementation, but may be in the future to ensure
   * appropriate formatting. It is acceptable for a contact to have a null or
   * empty email address.
   * 
   * @param newEmailAddress
   *          the email address to store
   */
  public void setEmailAddress(String newEmailAddress) {
    emailAddress = newEmailAddress;
  }

  private String note;

  /**
   * Gets the note associated with a contact. The note field can be null or
   * empty.
   * 
   * @return the (possibly null or empty) note text
   */
  public String getNote() {
    return note;
  }

  /**
   * Sets the note associated with this contact. Note text is not (nor likely
   * ever will be validated), so it is appropriate to store arbitrary text. The
   * note field can also be null or empty.
   * 
   * @param newNote
   *          arbitrary note text
   */
  public void setNote(String newNote) {
    note = newNote;
  }

  private PhoneNumber phoneNumber;

  /**
   * Gets the phone number associated with this contact.
   * 
   * @return the (possibly null) phone number associated with this contact
   */
  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Sets the phone number associated with this contact. It is possible for the
   * phone number to be null.
   * 
   * @param newPhoneNumber
   *          the new phone number for this contact
   */
  public void setPhoneNumber(PhoneNumber newPhoneNumber) {
    phoneNumber = newPhoneNumber;
  }

  private PostalAddress postalAddress;

  /**
   * Gets the mailing address associated with this contact.
   * 
   * @return the (possibly null) mailing address associated with this contact.
   */
  public PostalAddress getPostalAddress() {
    return postalAddress;
  }

  /**
   * Sets the mailing address associated with this contact. It is valid for the
   * mailing address to be null.
   * 
   * @param newPostalAddress
   *          new mailing address to use for this contact
   */
  public void setPostalAddress(PostalAddress newPostalAddress) {
    postalAddress = newPostalAddress;
  }

  /**
   * Contact is not designed to be publicly instantiable. Use the createNew()
   * static factory methods instead.
   * 
   * @see createNew
   */
  private Contact(ContactName name) {
    if (name == null) {
      throw new IllegalArgumentException("Name is required to be non-null"
          + "for any Contact");
    }
    this.name = name;
  }

  /**
   * Convenience method for easily creating a contact from just a first name.
   * Equivalent to {@code createWithName(firstName, null)}
   * 
   * @param firstName
   *          first name of the contact (required to be non-null)
   * @return new contact record with name populated
   */
  public static Contact createWithName(String firstName) {
    return createWithName(firstName, "");
  }

  /**
   * Convenience method for easily creating a new contact from just a name
   * 
   * @param firstName
   *          first name of the contact (required to be non-null)
   * @param lastName
   *          last name of the contact (possibly null)
   * @return new contact record with name populated
   */
  public static Contact createWithName(String firstName, String lastName) {
    ContactName name = new ContactName(firstName, lastName);
    return new Contact(name);
  }

  static final String XML_NAME = "Contact";
  private static final String EMAIL_XML_NAME = "Email";
  private static final String NOTE_XML_NAME = "Note";

  /**
   * Serializes this contact to an XML Element given the provided Docuemnt
   * context. A serialized contact can be deserialized using the static method
   * {@link fromXml(Element e)}
   * 
   * @param doc
   *          the context of the XML document to create the new XML Element with
   * @return an XML Element representing this contact
   * @see fromXml
   */
  Element toXmlElement(Document doc) {
    Element contactXml = doc.createElement(XML_NAME);
    contactXml.appendChild(name.toXmlElement(doc));
    if (postalAddress != null) {
      contactXml.appendChild(postalAddress.toXmlElement(doc));
    }
    if (phoneNumber != null) {
      contactXml.appendChild(phoneNumber.toXmlElement(doc));
    }

    {
      Element emailXml = doc.createElement(EMAIL_XML_NAME);
      emailXml.setTextContent(emailAddress);
      contactXml.appendChild(emailXml);
    }

    {
      Element noteXml = doc.createElement(NOTE_XML_NAME);
      noteXml.setTextContent(note);
      contactXml.appendChild(noteXml);
    }

    return contactXml;
  }

  /**
   * Deserializes a contact from the supplied XML element.
   * 
   * @param e
   *          the XML Element that represents a contact
   * @return a Contact with all fields represented by the XML populated
   */
  static Contact fromXml(Element e) {
    ContactName newName = ContactName.fromXml((Element) e.getElementsByTagName(
        "ContactName").item(0));
    Contact newContact = new Contact(newName);

    PostalAddress newAddress = PostalAddress.fromXml((Element) e
        .getElementsByTagName(PostalAddress.XML_NAME).item(0));
    newContact.setPostalAddress(newAddress);

    Element phoneNumberElement = (Element) e.getElementsByTagName(
        PhoneNumber.XML_NAME).item(0);
    if (phoneNumberElement != null) {
      newContact.setPhoneNumber(PhoneNumber.fromXml(phoneNumberElement));
    }

    String newEmail = e.getElementsByTagName("Email").item(0).getTextContent();
    newContact.setEmailAddress(newEmail);

    String newNote = e.getElementsByTagName("Note").item(0).getTextContent();
    newContact.setNote(newNote);
    return newContact;
  }

  /**
   * Provides a convenient way to sort Contact records by first name and then
   * last name
   */
  public static final Comparator<Contact> SORT_BY_FIRST_NAME = new FirstNameLastNameComparator();

  /**
   * Implements the first name, last name comparison for contacts
   * 
   * @see SORT_BY_FIRST_NAME
   */
  private static class FirstNameLastNameComparator implements
      Comparator<Contact> {
    @Override
    public int compare(Contact arg0, Contact arg1) {
      // ContactName implements breaking ties by comparing last names
      return ContactName.SORT_BY_FIRST_NAME.compare(arg0.getName(),
          arg1.getName());
    }
  } 

  /**
   * Provides a convenient way to sort Contact records by last name and then
   * first name
   */
  public static final Comparator<Contact> SORT_BY_LAST_NAME = new LastNameFirstNameComparator();

  /**
   * Implements the last name, first name comparison for contacts
   * 
   * @see SORT_BY_LAST_NAME
   */
  private static class LastNameFirstNameComparator implements
      Comparator<Contact> {
    @Override
    public int compare(Contact arg0, Contact arg1) {
      // ContactName implements breaking ties by comparing first names
      return ContactName.SORT_BY_LAST_NAME.compare(arg0.getName(),
          arg1.getName());
    }
  }

}