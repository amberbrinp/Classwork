/**
 * This class stores KeyedItem data in a reference based binary search tree.
 * A binary search tree is a binary tree (each node has 0, 1, or 2 children).
 * In addition, all items on the left of a given node have search keys that are "less than" the item at the specified node.
 * All items on the right of a given node have search keys that are "greater than" the item at the specified node.
 * Class invariant: BST rules maintained after any operation has been performed.
 */
public class BinarySearchTree extends BinaryTreeBasis implements SearchTreeInterface
{   
   /** The number of KeyedItems stored in the binary search tree.
    *  Class invariant: should be the same as the number of nodes in the tree.
    */
   private int size;

   /**  Constructs an empty binary search tree. */
   public BinarySearchTree()
   {
      super();
   }

   public KeyedItem retrieve(Comparable sk) 
   {
      return retrieveItem(getRootNode(), sk);
   }  

   public void insert(KeyedItem item) throws TreeException
   {
      setRootNode(insertItem(getRootNode(), item));
      size++;
      assert validateBSTProperty() : "not a binary search tree after insertion of key: " + item.getKey();
      assert validateSize() : "size is not correct after insertion of key: " + item.getKey();
   }  

   public void delete(Comparable sk) throws TreeException 
   {
      setRootNode(deleteItem(getRootNode(), sk));
      size--;
      assert validateBSTProperty() : "not a binary search tree after deletion of key: " + sk;
      assert validateSize() : "size is not correct after deletion of key: " + sk;
   }  

   /**
    * Recursive method to retrieve an item from the binary search tree. <br>
    * Preconditions: tNode is not null, sk is not null
    * Postconditions: the KeyedItem with the specified search key is returned, or null is returned if an item with the specified search key cannot be found
    */
   protected KeyedItem retrieveItem(TreeNode tNode, Comparable sk)
   {
      KeyedItem treeItem;

      if (tNode == null) 
      {
         treeItem = null;
      }
      else 
      {
         KeyedItem nodeItem = (KeyedItem) tNode.getItem();
         int comparison = sk.compareTo(nodeItem.getKey());

         if (comparison == 0) 
         {
            treeItem = nodeItem;
         }
         else if (comparison < 0) 
         {
            treeItem = retrieveItem(tNode.getLeft(), sk);
         }
         else  
         { 
            treeItem = retrieveItem(tNode.getRight(), sk);
         }  
      }  

      return treeItem;
   }  

   /**
    * Creates and returns a new TreeNode to be linked into the tree. <br>
    * Precondition: item is not null
    * Postcondition: a new TreeNode containing item is returned.
    * Note: Should be overridden by child classes to create TreeNodes of the correct type.
    */
   protected TreeNode createNode(KeyedItem item)
   {
      TreeNode tNode = new TreeNode(item);
      return tNode;
   }

   /**
    * Throws an exception if an attempt to insert an item with a search key that matches the
    * search key of an item already in the tree.
    * Override this method if duplicates are allowed in child classes.
    * Throws: TreeException as an attempt to insert a duplicate was detected.
    */
   protected TreeNode insertDuplicate(TreeNode tNode, KeyedItem item) throws TreeException
   {
      throw new TreeException ("Cannot add duplicate.");
   }

   /**
    * Searches for the leaf insertion location for item. <br>
    * Precondition: item is not null
    * Postcondition: returns the current node so that it can be linked (or relinked) to its parent
    * depending on whether a left or a right was taken in obtaining the leaf insertion location.
    * Throws: TreeException if an attempt to insert a duplicate is detected.
    * Calls: insertLeft, insertRight, insertDuplicate depending on the item's sk
    */
   protected TreeNode insertItem(TreeNode tNode, KeyedItem item) throws TreeException
   {
      if (tNode == null) 
      {
         return createNode(item);
      }  

      KeyedItem nodeItem = (KeyedItem) tNode.getItem();
      int comparison = item.getKey().compareTo(nodeItem.getKey());

      if (comparison == 0)
      {
         tNode = insertDuplicate(tNode, item);
      }
      else if (comparison < 0) 
      {
         tNode = insertLeft(tNode, item);
      }
      else 
      { 
         tNode = insertRight(tNode, item);
      }  

      return tNode;
   }  

   /**
    * A left hand turn must be dealt with to insert a node.
    * Preconditions: tNode is not null, item is not null
    * Postcondition: the child node is linked to tNode on the left, and tNode is returned to be linked in to its parent
    * Note: this method makes a recursive call to insertItem
    */
   protected TreeNode insertLeft(TreeNode tNode, KeyedItem item)
   {
      TreeNode subtree = insertItem(tNode.getLeft(), item);
      tNode.setLeft(subtree);
      return tNode;
   }

   protected TreeNode insertRight(TreeNode tNode, KeyedItem item)
   {
      TreeNode subtree = insertItem(tNode.getRight(), item);
      tNode.setRight(subtree);
      return tNode;
   }

   protected TreeNode deleteLeft(TreeNode tNode, Comparable sk)
   {
      TreeNode subtree = deleteItem(tNode.getLeft(), sk);
      tNode.setLeft(subtree);
      return tNode;
   }

   protected TreeNode deleteRight(TreeNode tNode, Comparable sk)
   {
      TreeNode subtree = deleteItem(tNode.getRight(), sk);
      tNode.setRight(subtree);
      return tNode;
   }

   protected TreeNode deleteItem(TreeNode tNode, Comparable sk) throws TreeException
   {
      if (tNode == null) 
      {
         throw new TreeException("Item not found");
      }

      TreeNode subtree;
      KeyedItem nodeItem = (KeyedItem)tNode.getItem();
      int comparison = sk.compareTo(nodeItem.getKey());

      if (comparison == 0) 
      {
         // item is in the root of some subtree
         tNode = deleteNode(tNode);  // delete the item
      }
      // else search for the item
      else if (comparison < 0) 
      {
         tNode = deleteLeft(tNode, sk);
      }
      else 
      { 
         tNode = deleteRight(tNode, sk);
      } 

      return tNode;
   }  

   protected TreeNode deleteNode(TreeNode tNode) 
   {
      TreeNode left = tNode.getLeft();
      TreeNode right = tNode.getRight();

      if (left == null) 
      {
         return tNode.getRight();
      } 
      else if (right == null) 
      {
         return tNode.getLeft();
      } 
      // there are two children:
      // retrieve and delete the inorder successor
      else 
      {
         return deleteInorderSuccessor(tNode);
      } 
   }  

   protected TreeNode deleteInorderSuccessor(TreeNode tNode)
   {
      TreeNode right = tNode.getRight();
      KeyedItem replacementItem = findLeftmost(right);
      tNode.setItem(replacementItem);
      TreeNode subtree = deleteLeftmost(tNode.getRight());
      tNode.setRight(subtree);
      return tNode;
   }

   protected KeyedItem findLeftmost(TreeNode tNode)  
   {
      if (tNode.getLeft() == null) 
      {
         return (KeyedItem) tNode.getItem();
      }
      else 
      {
         return findLeftmost(tNode.getLeft());
      }  
   } 

   protected TreeNode deleteLeftmost(TreeNode tNode) 
   {
      if (tNode.getLeft() == null) 
      {
         return tNode.getRight();
      }
      else 
      {
         return deleteLeftmostt(tNode);
      }  
   } 

   protected TreeNode deleteLeftmostt(TreeNode tNode)
   {
      TreeNode subtree = deleteLeftmost(tNode.getLeft());
      tNode.setLeft(subtree);
      return tNode;
   }

   protected TreeNode rotateLeft(TreeNode tNode)
   {
      TreeNode right = tNode.getRight();
      TreeNode rightleft = right.getLeft();

      tNode.setRight(rightleft);
      right.setLeft(tNode);

      return right;
   }

   protected TreeNode rotateRight(TreeNode tNode)
   {
      TreeNode left = tNode.getLeft();
      TreeNode leftright = left.getRight();

      tNode.setLeft(leftright);
      left.setRight(tNode);

      return left;
   }

   public boolean isBalanced()
   {
      return isBalanced(getRootNode());
   }

   protected boolean isBalanced(TreeNode tNode)
   {
      if (tNode == null)
      {
         return true;
      }

      boolean balanced = isBalanced(tNode.getLeft());
      if (!balanced) return balanced;
      balanced = isBalanced(tNode.getRight());
      if (!balanced) return balanced;

      int leftHeight = getHeight(tNode.getLeft());
      int rightHeight = getHeight(tNode.getRight());
      if (Math.abs(leftHeight - rightHeight) > 1)
      {
         balanced = false;
      }

      return balanced;
   }

   public int height()
   {
      return getHeight(getRootNode());
   }

   protected int getHeight(TreeNode tNode)
   {
      if (tNode == null)
      {
         return 0;
      }

      int height = 0;
      int leftHeight = getHeight(tNode.getLeft());
      int rightHeight = getHeight(tNode.getRight());
      if (leftHeight >= rightHeight)
      {
         height = leftHeight + 1;
      }
      else
      {
         height = rightHeight + 1;
      }

      return height;
   }

   public int size()
   {
      return size;
   }

   public int computeSize()
   {
      int num = 0;
      TreeIterator iter = (TreeIterator) iterator();
      iter.setInorder();

      while (iter.hasNext())
      {
         KeyedItem item = (KeyedItem) iter.next();
         num++;
      }

      return num;
   }

   public boolean validateSize()
   {
      return size() == computeSize();
   }

   public boolean validateBSTProperty()
   {
      TreeIterator iter = (TreeIterator) iterator();
      iter.setInorder();
      boolean valid = true;

      //an empty tree satisfies the BST property by definition
      if (!iter.hasNext()) return true;

      KeyedItem item1 = (KeyedItem) iter.next();
      Comparable key1 = item1.getKey();
      while (iter.hasNext())
      {
         KeyedItem item2 = (KeyedItem) iter.next();
         Comparable key2 = item2.getKey();

         //key1 must be <= key2 for BST property
         if (key1.compareTo(key2) > 0)
         {
            valid = false;
            break;
         }

         item1 = item2;
         key1 = key2;
      }

      return valid;
   }

} 