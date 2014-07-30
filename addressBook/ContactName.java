package unitTest.addressBook;

import java.util.Comparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents a ContactName as an aggregate first and last names
 * 
 * @author ck1456@nyu.edu
 * 
 * @see Contact
 */
public final class ContactName {
 
  /**
   * Provides a convenient way to sort Contact records by first name and then
   * last name
   * @see Contact
   */
  public static final Comparator<ContactName> SORT_BY_FIRST_NAME = 
      new FirstNameLastNameComparator();

  /**
   * Implements the last name, first name comparison for contacts
   * @see SORT_BY_FIRST_NAME
   */
  private static class FirstNameLastNameComparator implements
      Comparator<ContactName> {

    @Override
    public int compare(ContactName arg0, ContactName arg1) {
      if (arg0.getFirstName().equals(arg1.getFirstName())) {
        return arg0.getLastName().compareTo(arg1.getLastName());
      }
      return arg0.getFirstName().compareTo(arg1.getFirstName());
    }
  }
  
  /** 
   * Provides a convenient way to sort Contact records by last name and then
   * first name
   * @see Contact
   */ 
  public static final Comparator<ContactName> SORT_BY_LAST_NAME = 
      new LastNameFirstNameComparator();

  /**
   * Implements the last name, first name comparison for contacts
   * @see SORT_BY_LAST_NAME
   */
  private static class LastNameFirstNameComparator implements
      Comparator<ContactName> {

    @Override
    public int compare(ContactName arg0, ContactName arg1) {
      if (arg0.getLastName().equals(arg1.getLastName())) {
        return arg0.getFirstName().compareTo(arg1.getFirstName());
      }
      return arg0.getLastName().compareTo(arg1.getLastName());
    }
  }

  private String firstName;

  /**
   * Gets the first name for this contact.
   * @return the first name for this contact
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Updates the first name of this contact.  The first name is not permitted
   * to be null
   * @param firstName a non-null first name for the contact
   */
  public void setFirstName(String firstName) {
    if(firstName == null){
      throw new IllegalArgumentException("firstName is not allowed to be null");
    }
    this.firstName = firstName;
  }

  private String lastName;

  /**
   * Gets the last name for this contact contact last names may be null
   * @return the (possibly null) last name for this contact
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Updates the last name of this contact.  The last name may be null
   * @param lastName a last name for this contact
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Create a new contact name with a first name a last name
   * @param firstName a non-null first name for the contact
   * @param lastName a last name for the contact
   */
  public ContactName(String firstName, String lastName) {
    setFirstName(firstName);
    setLastName(lastName);
  }

  /**
   * Returns an intuitive representation of this name. The exact details of the
   * representation are subject to change, but the typically the result will be
   * "FirstName LastName" if a last name is specified (not null), and
   * "FirstName" otherwise
   */
  @Override
  public String toString() {
    if (lastName == null) {
      return firstName;
    } else {
      return String.format("%s %s", firstName, lastName);
    }
  }
 
  /**
   * Deserializes a ContactName from the supplied XML Element.
   * @param e the XML Element that represents a contact name
   * @return a ContactName with fields represented by the XML popoulated
   */
  static ContactName fromXml(Element e) {
    String firstName = e.getAttribute("FirstName");
    String lastName = e.getAttribute("LastName");
    ContactName newContactName = new ContactName(firstName, lastName);
    return newContactName;
  }

  /**
   * Serializes this ContactName to an XML Element using the provided
   * Document context
   * @param doc the context of the XML document to create the new XML element
   * @return an XML Element representing this contact name
   */
  Element toXmlElement(Document doc) {
    Element newElement = doc.createElement("ContactName");
    newElement.setAttribute("FirstName", firstName); 
    newElement.setAttribute("LastName", lastName);
    return newElement;
  }

}