package addressBook;

/**
 * An extensibility point for providing custom search logic when searching for
 * contacts in an {@link AddressBook}
 * @author ck1456@nyu.edu
 *
 */
public interface ISearchFilter {
  
  /**
   * A implementation of ISearchFilter should override this method to inspect
   * the provided contact to determine whether it is a match for the query text
   * according to the logic of the filter (i.e. matches a specific field).
   * 
   * @param query the text to use to match contact records
   * @param contact the {@link Contact} to test for a query match 
   * @return {@literal true} if the specified contact matches the {@code query}
   * according to the implementation of a search filter implementation,
   * {@literal false} otherwise 
   */
  boolean isMatch(String query, Contact contact);
}