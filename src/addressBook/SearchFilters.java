package addressBook;

/**
 * Exposes several predefined classes which implement {@link ISearchFilter} to
 * support determining which Contact records match given text on a specific
 * field  
 * 
 * @author ck1456@nyu.edu
 * @see ISearchFilter
 */
public final class SearchFilters {

  /**
   * Matches {@link Contact} records that contain a search string in either the
   * FirstName or LastName field
   */
  public static final ISearchFilter Name = new NameContactFilter();

  /** 
   * Matches {@link Contact} records that contain a search string in any
   * sub-field of a PostalAddress
   */
  public static final ISearchFilter PostalAddress = new PostalAddressFilter();
  
  /**
   * Matches {@link Contact} records that contain a search string in the
   * EmailAddress field
   */
  public static final ISearchFilter EmailAddress = new EmailAddressFilter();

  /**
   * Matches {@link Contact} records that contain a search string in the
   * note field
   */
  public static final ISearchFilter Note = new NoteFilter();
  
  /**
   * Matches {@link Contact} records that contain a search string in the
   * PhoneNumber field
   */
  public static final ISearchFilter PhoneNumber = new PhoneNumberFilter();

  /**
   * Matches {@link Contact} records that contain a search string in any
   * field
   */
  public static final ISearchFilter AnyField = new AnyFieldFilter();

  // Not instantiable
  private SearchFilters(){
    // No-op
  }
  
  private static class NameContactFilter implements ISearchFilter {
    @Override
    public boolean isMatch(String query, Contact contact) {
      try {
        ContactName name = contact.getName();
        if (name.getFirstName().contains(query)
            || name.getLastName().contains(query)) {
          return true;
        }
      } catch (Exception ex) {
        // We don't actually care why this failed, but we know it must not match
      }
      return false;
    }
  }

  private static class PostalAddressFilter implements ISearchFilter {
    @Override
    public boolean isMatch(String query, Contact contact) {
      try {
        PostalAddress address = contact.getPostalAddress();
        if (address.getAddressLine1().contains(query)
            || address.getAddressLine2().contains(query)
            || address.getCity().contains(query)
            || address.getState().contains(query)
            || address.getCountry().contains(query)
            || address.getPostalCode().contains(query)) {
          return true;
        }
      } catch (Exception ex) {
        // We don't actually care why this failed, but we know it must not match
      }
      return false;
    }
  }
  
  private static class EmailAddressFilter implements ISearchFilter {
    @Override
    public boolean isMatch(String query, Contact contact) {
      try {
        if (contact.getEmailAddress().contains(query)) {
          return true;
        }
      } catch (Exception ex) {
        // We don't actually care why this failed, but we know it must not match
      }
      return false;
    }
  }
  
  private static class NoteFilter implements ISearchFilter {
    @Override
    public boolean isMatch(String query, Contact contact) {
      try {
        if (contact.getNote().contains(query)) {
          return true;
        }
      } catch (Exception ex) {
        // We don't actually care why this failed, but we know it must not match
      }
      return false;
    }
  }
  
  private static class PhoneNumberFilter implements ISearchFilter {
    @Override
    public boolean isMatch(String query, Contact contact) {
      try {
        if (contact.getPhoneNumber().asString().contains(query)) {
          return true;
        }
      } catch (Exception ex) {
        // We don't actually care why this failed, but we know it must not match
      }
      return false;
    }
  }

  private static class AnyFieldFilter implements ISearchFilter {
    @Override
    public boolean isMatch(String query, Contact contact) {
      if (Name.isMatch(query, contact)
          || PostalAddress.isMatch(query, contact)
          || EmailAddress.isMatch(query, contact)
          || PhoneNumber.isMatch(query, contact)
          || Note.isMatch(query, contact)) {
        return true;
      }
      return false;
    }
  }
}