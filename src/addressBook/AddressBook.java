package addressBook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * An AddressBook provides a container for storing, managing and serializing a
 * collection of {@link Contact} records. Clients can search through an
 * AddressBook for Contact records based any field or optionally implement an
 * {@link ISearchFilter} to provide custom search logic.
 * A minimal example of using an AddressBook is as follows:
 * 
 * <pre>
 * {
 *   &#064;code
 *   AddressBook addressBook = AddressBook.createEmpty();
 *   addressBook.add(Contact.createWithName(&quot;John&quot;, &quot;Smith&quot;));
 *   addressBook.Save(&quot;contacts.xml&quot;);
 *   AddressBook addressBook2 = AddressBook.Load(&quot;contacts.xml&quot;);
 *   List&lt;Contact&gt; results = addressBook2.search(&quot;John&quot;);
 * }
 * </pre>
 * 
 * This class is not thread safe and was not designed to be inherited from.
 * 
 * @author ck1456@nyu.edu
 * 
 * @see Contact
 */
public final class AddressBook {

  /**
   * An AddressBook cannot be publicly instantiated. Use the static factory
   * method {@link #createEmpty()} to create a new empty AddressBook
   */
  private AddressBook() {
    // No-op
  }

  private final Set<Contact> contacts = new HashSet<Contact>();

  /**
   * Creates a new instance of an AddressBook that is initially empty
   * 
   * @return a new AddressBook with no Contact records
   */
  public static AddressBook createEmpty() {
    return new AddressBook();
  }

  /**
   * Adds the contact to the AddressBook. If contact already exists in the
   * AddressBook, the AddressBook is not modified.
   * 
   * @param contact
   *          a Contact record to store in the AddressBook
   */
  public void add(Contact contact) {
    contacts.add(contact);
  }

  /**
   * Removes a contact from the AddressBook. If the contact was not previously
   * in the AddressBook, the AddressBook is not modified.
   * 
   * @param contact
   *          the Contact record to remove
   */
  public void remove(Contact contact) {
    contacts.remove(contact);
  }

  /**
   * A convenience method for searching for an arbitrary string in all fields of
   * all contacts of the AddressBook. This is equivalent to
   * {@code search(query, SearchFilters.AnyField)}
   * 
   * @param query
   *          substring to search for
   * @return a (possibly empty) list of contact records that contain the query
   *         text in the specified fields
   */
  public List<Contact> search(String query) {
    return search(query, SearchFilters.AnyField);
  }

  /**
   * Search for text in arbitrary fields of all contact records of the
   * AddressBook. Predefined filters are available in the {@link SearchFilters}
   * class for doing single field searches.
   * 
   * @see SearchFilters
   * @param query
   *          substring to search for
   * @param filter
   * @return a (possibly empty) list of contact records that contain the query
   *         text in the specified fields
   */
  public List<Contact> search(String query, ISearchFilter filter) {
    List<Contact> results = new ArrayList<Contact>();
    for (Contact contact : contacts) {
      if (filter.isMatch(query, contact)) {
        results.add(contact);
      }
    }
    return results;
  }

  /**
   * Gets an unmodifiable view of all contact records in the AddressBook. By
   * default, this list is sorted by <last name>, <first name>
   * 
   * @return an unmodifiable List of contacts in the AddressBook
   */
  public List<Contact> getAllContacts() {
    List<Contact> contactListView = new ArrayList<Contact>(contacts);
    Collections.sort(contactListView, Contact.SORT_BY_LAST_NAME);
    return Collections.unmodifiableList(contactListView);
  }

  /**
   * Gets the number of contacts in the AddressBook
   * 
   * @return the number of contacts in the AddressBook
   */
  public int size() {
    return contacts.size();
  }

  /**
   * Provides a description of the state of this address book. The following
   * representation can be regarded as typical: "[AddressBook: 102 entries]"
   * 
   * @return summary description of address book
   */
  @Override
  public String toString() {
    return String.format("[AddressBook: %d entries]", size());
  }

  /**
   * Convenience method to parse an AddressBook from a file. This method calls
   * {@link #load(InputStream is)} internally.
   * 
   * @param filePath
   *          relative or absolute path to an xml file produced by serializing
   *          an AddressBook
   * @return an AddressBook in the same state prior to serialization
   * @throws FileNotFoundException
   * @throws SAXException
   * @throws IOException
   * @throws ParserConfigurationException
   */
  public static AddressBook load(String filePath) throws FileNotFoundException,
      SAXException, IOException, ParserConfigurationException {
    InputStream input = new FileInputStream(filePath);
    return load(input);
  }

  /**
   * Loads an AddressBook from an arbitrary InputStream source. This method is
   * used with {@code save(OutputStream os)} to perform deserialization.
   * 
   * @param is
   *          the stream to load from
   * @return a new AddressBook
   * @throws SAXException
   * @throws IOException
   * @throws ParserConfigurationException
   */
  public static AddressBook load(InputStream is) throws SAXException,
      IOException, ParserConfigurationException {

    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        .parse(is);
    Element root = doc.getDocumentElement();
    AddressBook addressBook = createEmpty();

    NodeList nodes = root.getElementsByTagName(Contact.XML_NAME);
    for (int i = 0; i < nodes.getLength(); i++) {
      Contact newContact = Contact.fromXml((Element) nodes.item(i));
      addressBook.add(newContact);
    }

    return addressBook;
  }

  /**
   * Convenience method for serializing an AddressBook to a file. This method
   * calls {@code save(OutputStream os)} internally.
   * 
   * @param filePath
   *          relative or absolute path to save the AddressBook to
   * @throws FileNotFoundException
   * @throws ParserConfigurationException
   * @throws TransformerFactoryConfigurationError
   * @throws TransformerException
   */
  public void save(String filePath) throws FileNotFoundException,
      ParserConfigurationException, TransformerFactoryConfigurationError,
      TransformerException {
    OutputStream os = new FileOutputStream(filePath);
    save(os);
  }

  private static final String XML_NAME = "AddressBook";

  /**
   * Stores an AddressBook representation into an arbitrary OutputStream. This
   * method can be used with {@code load(InputStream is)} to serialize an
   * AddressBook instance for persistence or transmission.
   * 
   * @param os
   *          the OutputStream to write into
   * @throws ParserConfigurationException
   * @throws TransformerFactoryConfigurationError
   * @throws TransformerException
   */
  public void save(OutputStream os) throws ParserConfigurationException,
      TransformerFactoryConfigurationError, TransformerException {

    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        .newDocument();
    Element root = doc.createElement(XML_NAME);
    doc.appendChild(root);

    Set<Contact> orderedContacts = new TreeSet<Contact>(
        Contact.SORT_BY_LAST_NAME);
    orderedContacts.addAll(contacts);
    for (Contact c : orderedContacts) {
      Element contactXml = c.toXmlElement(doc);
      root.appendChild(contactXml);
    }

    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

    transformer.transform(new DOMSource(doc), new StreamResult(os));
  }
}