package unitTest.addressBook;

import org.w3c.dom.*;

/**
 * Represents a mailing address.  All fields of a PostalAddress are guaranteed
 * to be non-null, but maybe empty if the record is not fully populated.
 * 
 * @author ck1456@nyu.edu
 *
 */
public class PostalAddress {

  private String addressLine1 = "";

  /**
   * Get the first address line
   * @return the first (primary) address line
   */
  public String getAddressLine1() {
    return addressLine1;
  }

  /**
   * Updates the primary address line.  Passing null to this method will cause
   * addressLine1 to be stored as an empty string.
   * @param addressLine1 the new primary line of the address
   */
  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = (addressLine1 == null ? "" : addressLine1);
  }

  private String addressLine2 = "";

  /**
   * Get the second address line
   * @return the second (optional) address line
   */
  public String getAddressLine2() {
    return addressLine2;
  }

  /**
   * Updates the second address line.  Passing null to this method will cause
   * addressLine2 to be stored as an empty string.
   * @param addressLine2 the new second line of the address
   */
  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = (addressLine2 == null ? "" : addressLine2);
  }

  private String city = "";

  /**
   * Get the city or town of this address
   * @return the city for this address
   */
  public String getCity() {
    return city;
  }

  /**
   * Updates the city or town.  Passing null to this method will cause
   * city to be stored as an empty string.
   * @param city the new city for the address
   */
  public void setCity(String city) {
    this.city = (city == null ? "" : city);
  }

  private String state = "";

  /**
   * Get the state or province.
   * @return the state or province for this address
   */
  public String getState() {
    return state;
  }

  /**
   * Update the state or province.  Passing null to this method will cause the
   * state to be stored as an empty string.
   * @param state the new state for the address
   */
  public void setState(String state) {
    this.state = (state == null ? "" : state);
  }

  private String country = "";

  /**
   * Get the country
   * @return the country for this address
   */
  public String getCountry() {
    return country;
  }

  /**
   * Updates the country.  Passing null to this method will cause the country
   * to be stored as an empty string.
   * @param country the new country for this address
   */
  public void setCountry(String country) {
    this.country = (country == null ? "" : country);
  }

  private String postalCode = "";

  /**
   * Get the postal or zip code.
   * @return the postal (zip) code for this address
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Updates the postal code.  Passing null to this method will cause the
   * postal code to be stored as an empty string.
   * @param postalCode the new postal code for this address
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = (postalCode == null ? "" : postalCode);
  }

  /**
   * Creates a new instance of a PostalAddress with all of the fields initially
   * empty.
   */
  public PostalAddress() {
    // No-op
    // Initial state invariants are set correctly in member declarations
  }

  /**
   * A convenience constructor that allows initializing all of the available
   * fields
   * 
   * @param addressLine1
   *          primary address line, typically required
   * @param addressLine2
   *          optional second address line
   * @param city
   *          city or town of the address
   * @param state
   *          the state or province of the address
   * @param country
   *          country of the address
   * @param postalCode
   *          postal code or zip code of the address
   */
  public PostalAddress(String addressLine1, String addressLine2, String city,
      String state, String country, String postalCode) {
    this.setAddressLine1(addressLine1);
    this.setAddressLine2(addressLine2);
    this.setCity(city);
    this.setState(state);
    this.setCountry(country);
    this.setPostalCode(postalCode);
  }

  /**
   * Convenience constructor for copying all of the fields of one PostalAddress
   * to another. This simply calls the fully parameterized constructor with the
   * appropriate arguments
   * 
   * @param other
   */
  public PostalAddress(PostalAddress other) {
    this(other.addressLine1, other.addressLine2, other.city, other.state,
        other.country, other.postalCode);
  }

  /**
   * A PostalAddress is equal to another address if all of the contained fields
   * are equal to the other.
   * @param o the object to compare to
   * @return true if all fields are identical, false otherwise 
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PostalAddress)) {
      return false;
    }
    PostalAddress other = (PostalAddress)o; 
    return (fieldEquals(addressLine1, other.addressLine1)
        && fieldEquals(addressLine2, other.addressLine2)
        && fieldEquals(city, other.city)
        && fieldEquals(state, other.state)
        && fieldEquals(country, other.country)
        && fieldEquals(postalCode, other.postalCode));
  }

  private static<T> boolean fieldEquals(T a, T b){
    return (a == null ? b == null : a.equals(b));
  }
  
  /**
   * Calculates a reasonable hash value based on the contents of all contained
   * fields
   */
  @Override public int hashCode(){
    int result = 17;
    result = 31 * result + fieldHashCode(addressLine1);
    result = 31 * result + fieldHashCode(addressLine2);
    result = 31 * result + fieldHashCode(city);
    result = 31 * result + fieldHashCode(state);
    result = 31 * result + fieldHashCode(country);
    result = 31 * result + fieldHashCode(postalCode);
    return result;
  }
  
  private static int fieldHashCode(Object o){
    return (o == null ? 0 : o.hashCode());
  }

  /**
   * Provides a useful mailing address-like representation of this address
   */
  @Override public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append(addressLine1).append("\n");
    if(!addressLine1.isEmpty()){
      sb.append(addressLine2).append("\n");
    }
    sb.append(city).append(",").append(state).append(" ");
    sb.append(postalCode).append("\n");
    if(!country.isEmpty()){
      sb.append(country).append("\n");
    }
    
    return sb.toString();
  }
  
  /**
   * Deserializes a postal address from the supplied XML element.
   * @param e the XML Element that represents a postal address
   * @return a PostalAddress with all fields represented by the XML populated
   */
  static PostalAddress fromXml(Element e) {
    if(e == null){
      return null; 
    }
    PostalAddress newAddress = new PostalAddress();
    newAddress.addressLine1 = e.getAttribute("AddressLine1");
    newAddress.addressLine2 = e.getAttribute("AddressLine2");
    newAddress.city = e.getAttribute("City");
    newAddress.state = e.getAttribute("State");
    newAddress.country = e.getAttribute("Country");
    newAddress.postalCode = e.getAttribute("PostalCode");
    return newAddress;
  }
 
  static final String XML_NAME = "PostalAddress";
  
  /**
   * Serializes the postal address to an XML Element given the provided
   * Document context.
   * @param doc the context of the XML document to create the XML Element
   * with
   * @return an XML Element representing this postal address
   */
  Element toXmlElement(Document doc) {
    Element newElement = doc.createElement(XML_NAME);
    newElement.setAttribute("AddressLine1", addressLine1);
    newElement.setAttribute("AddressLine2", addressLine2);
    newElement.setAttribute("City", city);
    newElement.setAttribute("State", state);
    newElement.setAttribute("Country", country);
    newElement.setAttribute("PostalCode", postalCode);
    return newElement;
  }
}