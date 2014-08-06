package addressBook;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

/**
 * Represents an immutable phone number instance. While PhoneNumbers are
 * typically transferred as Strings as input and output of this class, the
 * internal representation ensures valid phone number syntax for storing and
 * serializing.
 * 
 * This class depends on Google's lipphonenumber library:
 * http://code.google.com/p/libphonenumber/
 * As governed by the Apache License v 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * @author ck1456@nyu.edu
 */
public final class PhoneNumber {

  private final com.google.i18n.phonenumbers.Phonenumber
      .PhoneNumber phoneNumber;

  private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil
      .getInstance();

  // Not publicly instantiable
  private PhoneNumber(
      com.google.i18n.phonenumbers.Phonenumber.PhoneNumber number) {
    phoneNumber = number;
  }

  /**
   * Create a new strongly-typed PhoneNumber by parsing the supplied string
   * representation
   * 
   * @param phoneNumber string representation of a phone number
   * @return an immutable PhoneNumber
   * @throws Exception if the number cannot be parsed or is invalid
   */
  public static PhoneNumber createNew(String phoneNumber) throws Exception {
    try {
      return new PhoneNumber(phoneUtil.parse(phoneNumber, "US"));
    } catch (NumberParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new Exception("Cannot Parse Number");
    }
  } 

  /**
   * Convenience method to suppress throwing an Exception if
   * {@code createNew(String phoneNumber)} cannot successfully parse the string
   * representation of a phone number.
   * This methods returns null instead of throwing an exception.
   * 
   * @param phoneNumber string representation of a phone number
   * @return an immutable PhoneNumber or null (on error)
   */
  public static PhoneNumber tryCreateNew(String phoneNumber) {
    try {
      return new PhoneNumber(phoneUtil.parse(phoneNumber, "US"));
    } catch (NumberParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Gets a the canonical simple string representation of this PhoneNumber
   * @return a string representing the phonenumber
   */
  public String asString() {
    return "" + phoneNumber.getNationalNumber();
  }

  /**
   * Gets the complete description of this PhoneNumber
   * @return a string detailing the country code and national number of this
   * PhoneNumber instance
   */
  @Override
  public String toString() {
    return phoneNumber.toString();
  }

  static final String XML_NAME = "PhoneNumber";
  private static final String NUMBER_XML_NAME = "FormattedString";
  
  /**
   * Serializes this phone number to an XML Element given the provided Document
   * context.  A serialized phone number can be deserialized using the static
   * method {@link fromXml(Element e)}
   * @param doc the context of the XML document to create the new XML Element
   * with
   * @return an XML Element representing this phone number
   * @see fromXml
   */
  Element toXmlElement(Document doc) {
    Element newElement = doc.createElement(XML_NAME);
    newElement.setAttribute(NUMBER_XML_NAME, asString());
    return newElement;
  }
  
  /**
   * Deserializes a phone number from the supplied XML element.
   * @param e the XML Element that represents a phone number
   * @return a PhoneNumber parsed from the XML
   */
  static PhoneNumber fromXml(Element e) {
    String number = e.getAttribute(NUMBER_XML_NAME);
    return PhoneNumber.tryCreateNew(number);
  }
  
}