package unitTest.addressBook;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class AddressBookTest {
	AddressBook addressBook;
	Contact contact1;
	Contact contact2;
	Contact contact3;
	
	@Before
	public void setUp() throws Exception {
		contact1 = Contact.createWithName("William", "Gates");
		contact1.setEmailAddress("wg1544@hotmail.com");
		contact1.setNote("the first contact inserted");
		contact1.setPhoneNumber(PhoneNumber.createNew("2127740908"));
		contact1.setPostalAddress(new PostalAddress(
				"40 Broadway", "NYU", "New York", "NY", "US", "10121"));
		
		contact2 = Contact.createWithName("Pepper");
		contact2.setEmailAddress("pepper@microsoft.com");
		contact2.setPhoneNumber(PhoneNumber.createNew("7149883232"));
		contact2.setPostalAddress(new PostalAddress(
				"14 52nd Street", "NYU", "New York", "NY", "US", "10016"));
		
		contact3 = Contact.createWithName("Zach", "Wolfe");
		contact3.setEmailAddress("wolfe22@gmail.com");
		contact3.setNote("family account");
		contact3.setPhoneNumber(PhoneNumber.createNew("2018450098"));
		contact3.setPostalAddress(new PostalAddress(
				"35 River Court Apt1621", "NYU", "Jersey City", "NJ", "US", "07311"));
		
		addressBook = AddressBook.createEmpty();
		addressBook.add(contact1);
		addressBook.add(contact2);
		addressBook.add(contact3);

	}
	
	@Test
	public void testCreateEmpty() {
		AddressBook emptyBook = AddressBook.createEmpty();
		List<Contact> contactList = emptyBook.getAllContacts();
		assertTrue(contactList.isEmpty());
	}
	
	@Test
	public void testAdd() {
		List<Contact> contactList = addressBook.getAllContacts();
		assertTrue(contactList.contains(contact1));
		assertTrue(contactList.contains(contact2));
		assertTrue(contactList.contains(contact3));
	}
	
	@Test 
	public void testRemove() {
		addressBook.remove(contact1);
		List<Contact> contactList = addressBook.getAllContacts();
		assertFalse(contactList.contains(contact1));		
	}
	
	@Test
	public void testSearch() {
		List<Contact> result= addressBook.search("William");
		assertTrue(result.contains(contact1));
	}
	
	@Test
	public void testSearchByFilter() {
		List<Contact> result = addressBook.search("pepper", SearchFilters.EmailAddress);
		assertTrue(result.contains(contact2));
		result = addressBook.search("NYU", SearchFilters.PostalAddress);
		assertTrue(result.contains(contact1));
		assertTrue(result.contains(contact2));
		assertTrue(result.contains(contact3));
	}
	
	@Test
	public void testSize() {
		assertEquals(3, addressBook.size());
	}
	
	@Test
	public void testSaveToFile() throws FileNotFoundException, 
			ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		addressBook.save("addressBookTest.xml");
	}
	
	@Test
	public void testSaveToOutputStream() throws ParserConfigurationException, 
			TransformerFactoryConfigurationError, TransformerException, IOException {
		File file = new File("AddressBookTest.xml");
		OutputStream os = new FileOutputStream(file);
		try {
			addressBook.save(os);
		}finally {
			os.close();
		}
	}
	
	@Test
	public void testLoadFromFile() throws FileNotFoundException, SAXException, 
			IOException, ParserConfigurationException {
		AddressBook.load("addressBookTest.xml");
	}
	
	@Test
	public void testLoadFromInputStream() throws SAXException, IOException,
			ParserConfigurationException {
		File file = new File("AddressBookTest.xml");
		InputStream is = new FileInputStream(file);
		AddressBook.load(is);
	}
}
