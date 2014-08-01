package unitTest.addressBook;

import static org.junit.Assert.*;

import org.junit.Test;

public class PhoneNumberTest {

	@Test
	public void testCreateNew() throws Exception {
		PhoneNumber ph = PhoneNumber.createNew("2017740908");
		assertEquals("2017740908", ph.asString());
		try {
		PhoneNumber.createNew("adbl");
		fail();
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testTryCreateNew() {
		PhoneNumber ph = PhoneNumber.tryCreateNew("212-514-0098");
		assertEquals("2125140098", ph.asString());
		PhoneNumber ph2 = PhoneNumber.tryCreateNew("tls");
		assertNull(ph2);
	}
}
