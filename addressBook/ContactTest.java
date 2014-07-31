package unitTest.addressBook;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ContactTest {
	private Contact contact;
	
	@Before
	public void setup() {
		contact = Contact.createWithName("William", "Gates");
	}
	
	@Test
	public void testSetAndGetName() {
		contact.setName(new ContactName("Alicia", "Florick"));
		assertEquals("Alicia", contact.getName().getFirstName());
		assertEquals("Florick", contact.getName().getLastName());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentExceptionThrownBySetName() {
		contact.setName(null);
	}
	
	@Test 
	public void testToXmlElement() throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().
				newDocumentBuilder().newDocument();
		contact.setEmailAddress("wg1454@nyu.edu");
		contact.setNote("appointe scheduled on Thursday");
		PhoneNumber p = PhoneNumber.createNew("2123475562");
		contact.setPhoneNumber(p);
		PostalAddress pa = new PostalAddress("1062 Broadyway", "Suite301", "New York",
				"NY", "US", "10312");
		contact.setPostalAddress(pa);
		
		Element e = contact.toXmlElement(doc);
		
		Element name = (Element) e.getElementsByTagName("ContactName").item(0);
		assertEquals(name.getAttribute("FirstName"), "William");
		assertEquals(name.getAttribute("LastName"), "Gates");
		
		Element postalAddress = (Element) e.getElementsByTagName("PostalAddress").item(0);
		assertEquals(postalAddress.getAttribute("AddressLine1"), "1062 Broadyway");
		assertEquals(postalAddress.getAttribute("AddressLine2"), "Suite301");
		assertEquals(postalAddress.getAttribute("City"), "New York");
		assertEquals(postalAddress.getAttribute("State"), "NY");
		assertEquals(postalAddress.getAttribute("Country"), "US");
		assertEquals(postalAddress.getAttribute("PostalCode"), "10312");
		
		Element phoneNumEle = (Element) e.getElementsByTagName("PhoneNumber").item(0);
		PhoneNumber phoneNum = PhoneNumber.fromXml(phoneNumEle);
		assertEquals(phoneNum.asString(), "2123475562");
		
		String email = e.getElementsByTagName("Email").item(0).getTextContent();
		assertEquals(email, "wg1454@nyu.edu");
		
		String note = e.getElementsByTagName("Note").item(0).getTextContent();
		assertEquals(note, "appointe scheduled on Thursday");
	}
	
	private Element createContactElement() throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().
				newDocumentBuilder().newDocument();
		contact.setEmailAddress("wg1454@nyu.edu");
		contact.setNote("appointe scheduled on Thursday");
		PhoneNumber p = PhoneNumber.createNew("2123475562");
		contact.setPhoneNumber(p);
		PostalAddress pa = new PostalAddress("1062 Broadyway", "Suite301", "New York",
				"NY", "US", "10312");
		contact.setPostalAddress(pa);
		
		return contact.toXmlElement(doc);
	}
	
	@Test
	public void testFromXmlElement() throws Exception {
		Element contactEle = createContactElement();
		Contact contact = Contact.fromXml(contactEle);
		assertEquals("William", contact.getName().getFirstName());
		assertEquals("Gates", contact.getName().getLastName());
		assertEquals("wg1454@nyu.edu", contact.getEmailAddress());
		assertEquals("2123475562", contact.getPhoneNumber().asString());
		assertEquals("appointe scheduled on Thursday", contact.getNote());
	}

	@Test
  public void testFirstNameLastNameComparator() {
		Contact contact1 = Contact.createWithName("William", "Gates");
		Contact contact2 = Contact.createWithName("Pepper", "Joy");
		Contact contact3 = Contact.createWithName("Zach", "Wolfe");
		
		List<Contact> contactList = new ArrayList<Contact>();
		contactList.add(contact1);
		contactList.add(contact2);
		contactList.add(contact3);
		
		Collections.sort(contactList, Contact.SORT_BY_FIRST_NAME);
		assertEquals("Pepper", contactList.get(0).getName().getFirstName());
		assertEquals("William", contactList.get(1).getName().getFirstName());
		assertEquals("Zach", contactList.get(2).getName().getFirstName());
  }
	
	@Test 
	public void testLastNameFirstNameComparator() {
		Contact contact1 = Contact.createWithName("William", "Gates");
		Contact contact2 = Contact.createWithName("Pepper", "Joy");
		Contact contact3 = Contact.createWithName("Zach", "Wolfe");
		
		List<Contact> contactList = new ArrayList<Contact>();
		contactList.add(contact1);
		contactList.add(contact2);
		contactList.add(contact3);
		
		Collections.sort(contactList, Contact.SORT_BY_LAST_NAME);
		assertEquals("Gates", contactList.get(0).getName().getLastName());
		assertEquals("Joy", contactList.get(1).getName().getLastName());
		assertEquals("Wolfe", contactList.get(2).getName().getLastName());
	}
}
