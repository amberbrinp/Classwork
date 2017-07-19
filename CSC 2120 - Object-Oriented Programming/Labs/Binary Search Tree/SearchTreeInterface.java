
/**
 *  This interface defines the operations for any tree that stores
 *  KeyedItems based on the values of their keys (value-oriented).
 */
public interface SearchTreeInterface 
{

   /**
    * Returns the item stored at the root.
    */
   public Object getRootItem() throws TreeException;

   /**
    * Determines whether a tree is empty.
    * Preconditions: None.
    * Postcondition: Returns true if the tree is empty; otherwise returns false.
    */
   public boolean isEmpty();

   /**
    * Returns the number of keyed items in the tree.
    * Preconditions: None.
    * Postcondition: Returns the number of keyed items in the tree.
    */
   public int size();

   /**
    * Inserts an item into a tree in its proper sorted order according to the item's search key.
    * Precondition: The item to be inserted into the tree has a search key that differs from all search keys presently in the table.
    * Postcondition: If the insertion was successful, item is in its proper order in the tree.  Otherwise, the tree is unchanged.
    * Throws: TreeException if the search key is already present
    */
   public void insert(KeyedItem item) throws TreeException;

   /**
    * Deletes an item with a given search key from the tree.
    * Precondition: searchKey is the search key of the item to be deleted.
    * Postcondition: If the item whose search key equals searchKey existed in the tree, the item was deleted. Otherwise, the tree is unchanged.
    * Throws: TreeException if the search key is not in the tree.
    */
   public void delete(Comparable searchKey) throws TreeException;

   /**
    * Retrieves an item with a given search key from the tree.
    * Precondition: searchKey is the search key of the item to be retrieved.
    * Postcondition: If the retrieval was successful, the tree item with the matching search key is returned. If no such item exists, the method returns a null reference.
    */
   public KeyedItem retrieve(Comparable searchKey);

   /**
    * Removes all the tree contents.
    * Precondition: None
    * Postcondition: The tree is empty.
    */
   public void makeEmpty();

   /**
    * Returns the TreeIterator for the active tree
    * Preconditions: None
    * Postconditions: The TreeIterator is returned.
    */
   public TreeIterator iterator();

} 