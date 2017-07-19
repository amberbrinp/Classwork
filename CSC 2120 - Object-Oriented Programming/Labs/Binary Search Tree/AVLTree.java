
public final class AVLTree extends BinarySearchTree
{
   //keep checking balancing rules?
   private boolean avlFlag;

   public AVLTree()  
   {
       super();
   } 

   public void insert(KeyedItem item) throws TreeException
   {
       super.insert(item);
       avlFlag = false;
       assert isBalanced() : "tree not balanced";
   } 

   public void delete(Comparable sk) throws TreeException
   {
       super.delete(sk);
       avlFlag = false;
       assert isBalanced() : "tree not balanced";
   }

   //want to override, not overload
   /*protected TreeNode insertItem(TreeNode tNode, KeyedItem item) 
   {
      TreeNode subtree;
      if (tNode == null) 
      {
         // position of insertion found; insert after leaf
         // create a new node
         tNode = new AVLTreeNode(item);  //set to balanced in the constructor
         avlFlag = true; //added a node to the tree, need to check balancing
         return tNode;
      } 

      KeyedItem nodeItem = (KeyedItem) tNode.getItem();
      int comparison = item.getKey().compareTo(nodeItem.getKey());

      // search for the insertion position
      if (comparison == 0)
      {
         throw new TreeException ("Cannot add duplicate.");
      }
      else if (comparison < 0) 
      {
         // search the left subtree
         subtree = insertItem(tNode.getLeft(), item);
         tNode.setLeft(subtree);

         //check balance factor and rotate if necessary
         if (avlFlag)  //still need to check
         {
            tNode = avlFixAdd((AVLTreeNode)tNode, 1);  //1 indicates came from the left
         }
      }
      else 
      { 
         // search the right subtree
         subtree = insertItem(tNode.getRight(), item);
         tNode.setRight(subtree);

         //check balance factor and rotate if necessary
         if (avlFlag)
         {
            tNode = avlFixAdd((AVLTreeNode)tNode, 2);  //2 indicates came from the right
         }
      } 

      return tNode;
   } */

   protected TreeNode deleteItem(TreeNode tNode, Comparable searchKey) 
   {
      TreeNode subtree;
      if (tNode == null) 
      {
         throw new TreeException("Item not found");
      }

      KeyedItem nodeItem = (KeyedItem) tNode.getItem();
      int comparison = searchKey.compareTo(nodeItem.getKey());

      if (comparison == 0) 
      {
         // item is in the root of some subtree
         //found something to delete so set avlFlag to true
         avlFlag = true;  //will delete a node from the tree, need to check balancing
         tNode = deleteNode(tNode);  // delete the item
      }
      // else search for the item
      else if (comparison < 0) 
      {
         // search the left subtree
         subtree = deleteItem(tNode.getLeft(), searchKey);
         tNode.setLeft(subtree);

         //check balance factors and rotate if necessary
         if (avlFlag)
         {
            tNode = avlFixDelete((AVLTreeNode)tNode, 1);  //came from left
         }
      }
      else 
      { 
         // search the right subtree
         subtree = deleteItem(tNode.getRight(), searchKey);
         tNode.setRight(subtree);

         //check balance factors and rotate if necessary
         if (avlFlag)
         {
            tNode = avlFixDelete((AVLTreeNode)tNode, 2);  //came from right
         }
      }  

      return tNode;
   } 

   //determine if the delete is an easy case or a hard case
   protected TreeNode deleteNode(TreeNode tNode) 
   {
      KeyedItem replacementItem;

      // test for a leaf --  this test is taken care of by the next two
      if ( (tNode.getLeft() == null) && (tNode.getRight() == null) ) 
      {
         return null;
      }  

      // test for no left child
      else if (tNode.getLeft() == null) 
      {
         return tNode.getRight();
      } 

      // test for no right child
      else if (tNode.getRight() == null) 
      {
         return tNode.getLeft();
      }  

      // there are two children:
      // retrieve and delete the inorder successor
      else 
      {
         replacementItem = findLeftmost(tNode.getRight());
         tNode.setItem(replacementItem);
         tNode.setRight(deleteLeftmost(tNode.getRight()));
         if (avlFlag)
         {
            //inorder successor: take a right, then keep going left
            tNode = avlFixDelete((AVLTreeNode)tNode, 2);  //came from right
         }
         return tNode;
      }  
   } 

   //find the inorder successor
   //this method is overridden to allow checking for balancing
   protected TreeNode deleteLeftmost(TreeNode tNode) 
   {
      if (tNode.getLeft() == null) 
      {
         avlFlag = true;
         return tNode.getRight();
      }
      else 
      {
         tNode.setLeft(deleteLeftmost(tNode.getLeft()));
         if (avlFlag)
         {
            tNode = avlFixDelete((AVLTreeNode)tNode, 1);  //keep going left
         }
         return tNode;
      }  
   }   

   protected AVLTreeNode fixAddLeft(AVLTreeNode tNode)
   {
      tNode.insertLeft();
      AVL factor = tNode.getBalanceFactor();

      if (factor == AVL.BALANCED)  //change to balanced, stop
      {
          avlFlag = false;
          return tNode;
      }
      else if (factor == AVL.LEFT || factor == AVL.RIGHT)
      {
          return tNode;
      }
      else
      {
          AVLTreeNode right = tNode.getRight();
          AVLTreeNode left = tNode.getLeft();
          AVLTreeNode temp;

          //double rotate right then left
          if (factor == AVL.RIGHT_HEAVY && right.getBalanceFactor() == AVL.LEFT)
          {
              temp = doubleRightRotation(tNode);
          }
          else if (factor == AVL.LEFT_HEAVY && left.getBalanceFactor() == AVL.RIGHT)
          {
              temp = doubleLeftRotation(tNode);
          }
          //single rotate right
          else if (factor == AVL.LEFT_HEAVY && left.getBalanceFactor() == AVL.LEFT)
          {
              temp = singleRightRotation(tNode);
          }
          else
          {
              temp = singleLeftRotation(tNode);
          }
          avlFlag = false;
          return temp;
      }
   }

   protected AVLTreeNode fixAddRight(AVLTreeNode tNode)
   {
      tNode.insertRight();
      AVL factor = tNode.getBalanceFactor();

      if (factor == AVL.BALANCED)  //change to balanced, stop
      {
         avlFlag = false; 
         return tNode;
      }

      //no rotation necessary at this node, but need to keep checking upwards
      else if (factor == AVL.LEFT || factor == AVL.RIGHT)
      {
         return tNode;  
      }
      else
      {
         AVLTreeNode right = tNode.getRight();
         AVLTreeNode left = tNode.getLeft();
         AVLTreeNode temp;
         //double rotate right then left
         if (factor == AVL.RIGHT_HEAVY && right.getBalanceFactor() == AVL.LEFT)
         {
            temp = doubleRightRotation(tNode);
         }
         else if (factor == AVL.LEFT_HEAVY && left.getBalanceFactor() == AVL.RIGHT)
         {
            temp = doubleLeftRotation(tNode);
         }

         //single rotate right
         else if (factor == AVL.LEFT_HEAVY && left.getBalanceFactor() == AVL.LEFT)
         {
            temp = singleRightRotation(tNode);
         }
         else
         {
            temp = singleLeftRotation(tNode);
         }
         avlFlag = false;
         return temp;
      }
   }

   protected AVLTreeNode avlFixDelete(AVLTreeNode tNode, int dir)
   {
      AVL factor;
      AVLTreeNode temp;

      //first must update the bance factors
      if (dir == 1)  //came from the left
      {
         factor = tNode.getBalanceFactor();
         tNode.deleteLeft();
         if (factor == AVL.BALANCED)  //change from zero--  STOP
         {
            avlFlag = false; //no more to do this time around
            return tNode;
         }
         factor = tNode.getBalanceFactor();
      }
      else  //came from the right
      {
         factor = tNode.getBalanceFactor();
         tNode.deleteRight();
         if (factor == AVL.BALANCED) //change from zero--  STOP
         {
            avlFlag = false; //no more to do this time around
            return tNode;
         }
         factor = tNode.getBalanceFactor();
      }

      if (factor == AVL.LEFT || factor == AVL.RIGHT || factor == AVL.BALANCED)
      {
         return tNode;  //need to keep checking, but no rotations necessary as yet
      }
      else
      {
         //rotations necessary for deleting a node
         AVLTreeNode right = tNode.getRight();
         AVLTreeNode left = tNode.getLeft();

         if (factor == AVL.LEFT_HEAVY && left.getBalanceFactor() == AVL.BALANCED)
         {
            tNode.setBalanceFactor(AVL.LEFT);
            left.setBalanceFactor(AVL.RIGHT);
            temp = (AVLTreeNode) rotateRight(tNode);
            avlFlag = false;  //STOP
            System.out.println("SR0");
         }

         else if (factor == AVL.RIGHT_HEAVY && right.getBalanceFactor() == AVL.BALANCED)
         {
            tNode.setBalanceFactor(AVL.RIGHT);
            right.setBalanceFactor(AVL.LEFT);
            temp = (AVLTreeNode) rotateLeft(tNode);
            avlFlag = false;  //STOP
            System.out.println("SL0");
         }

         else if (factor == AVL.LEFT_HEAVY && left.getBalanceFactor() == AVL.LEFT)
         {
            tNode.setBalanceFactor(AVL.BALANCED);
            left.setBalanceFactor(AVL.BALANCED);
            temp = (AVLTreeNode) rotateRight(tNode);
            System.out.println("SR");
         }

         else if (factor == AVL.RIGHT_HEAVY && right.getBalanceFactor() == AVL.RIGHT)
         {
            tNode.setBalanceFactor(AVL.BALANCED);
            right.setBalanceFactor(AVL.BALANCED);
            temp = (AVLTreeNode) rotateLeft(tNode);
            System.out.println("SL");
         }

         else if (factor == AVL.LEFT_HEAVY && left.getBalanceFactor() == AVL.RIGHT)
         {
            AVLTreeNode leftRight = left.getRight();
            AVL bF = leftRight.getBalanceFactor();
        
            if (bF == AVL.BALANCED) 
            {
               tNode.setBalanceFactor(AVL.BALANCED);
               left.setBalanceFactor(AVL.BALANCED);
            }
            else if (bF == AVL.RIGHT)
            {
               tNode.setBalanceFactor(AVL.BALANCED);
               left.setBalanceFactor(AVL.LEFT);
            }
            else
            {
               tNode.setBalanceFactor(AVL.RIGHT);  
               left.setBalanceFactor(AVL.BALANCED);  
            }

            leftRight.setBalanceFactor(AVL.BALANCED);

            temp = (AVLTreeNode) rotateLeft(left);
            tNode.setLeft(temp);
            temp = (AVLTreeNode) rotateRight(tNode);
            System.out.println("DLR");
         }

         else
         {
            AVLTreeNode rightLeft = right.getLeft();
            AVL bF = rightLeft.getBalanceFactor();

            if (bF == AVL.BALANCED)  
            {
               tNode.setBalanceFactor(AVL.BALANCED);
               right.setBalanceFactor(AVL.BALANCED);
            }
            else if (bF == AVL.RIGHT)
            {
               tNode.setBalanceFactor(AVL.LEFT);
               right.setBalanceFactor(AVL.BALANCED);
            }
            else
            {
               tNode.setBalanceFactor(AVL.BALANCED);
               right.setBalanceFactor(AVL.RIGHT);
            }
            rightLeft.setBalanceFactor(AVL.BALANCED);

            temp = (AVLTreeNode) rotateRight(right);
            tNode.setRight(temp);
            temp = (AVLTreeNode) rotateLeft(tNode);
            System.out.println("DRL");
         }

         return temp;
      }
   }
   protected TreeNode createNode(KeyedItem item)
   {
     // System.out.println("Went to Create Node");
      TreeNode tNode = new AVLTreeNode(item);
      avlFlag = true;
      return tNode;
   }

   protected TreeNode insertLeft(TreeNode tNode, KeyedItem item)
   {
      tNode = super.insertLeft(tNode,item);
      if (avlFlag) // still need to check
      {
         tNode = fixAddLeft((AVLTreeNode)tNode); //indicates came from the left
      }
      return tNode;
   }

   protected TreeNode insertRight(TreeNode tNode, KeyedItem item)
   {
      tNode = super.insertRight(tNode, item);
      if (avlFlag)
      {
         tNode = fixAddRight((AVLTreeNode)tNode); //indicates came from the right
      }
      return tNode;
   }

   protected AVLTreeNode doubleRightRotation(AVLTreeNode tNode)
   {
      AVLTreeNode right = tNode.getRight();
      AVLTreeNode rightLeft = right.getLeft();
      AVL bF = rightLeft.getBalanceFactor();

      if (bF == AVL.BALANCED)  //can happen
      {
         tNode.setBalanceFactor(AVL.BALANCED);
         right.setBalanceFactor(AVL.BALANCED);
      }
      else if (bF == AVL.RIGHT)
      {
         tNode.setBalanceFactor(AVL.LEFT);
         right.setBalanceFactor(AVL.BALANCED);
      }
      else
      {
         tNode.setBalanceFactor(AVL.BALANCED);
         right.setBalanceFactor(AVL.RIGHT);
      }

      rightLeft.setBalanceFactor(AVL.BALANCED);

      AVLTreeNode temp = (AVLTreeNode) rotateRight(right);
      tNode.setRight(temp);
      temp = (AVLTreeNode) rotateLeft(tNode);
      System.out.println("DRL");
      assert isBalanced(temp) : "AVLTree not balanced";
      return temp;
   }

   protected AVLTreeNode doubleLeftRotation(AVLTreeNode tNode)
   {
      AVLTreeNode left = tNode.getLeft();
      AVLTreeNode leftRight = left.getRight();
      AVL bF = leftRight.getBalanceFactor();

      if (bF == AVL.BALANCED)  //can happen
      {
         tNode.setBalanceFactor(AVL.BALANCED);
         left.setBalanceFactor(AVL.BALANCED);
      }
      else if (bF == AVL.RIGHT)
      {
         tNode.setBalanceFactor(AVL.BALANCED);
         left.setBalanceFactor(AVL.LEFT);
      }
      else
      {
         tNode.setBalanceFactor(AVL.RIGHT);
         left.setBalanceFactor(AVL.BALANCED);
      }

      leftRight.setBalanceFactor(AVL.BALANCED);

      AVLTreeNode temp = (AVLTreeNode) rotateLeft(left);
      tNode.setLeft(temp);
      temp = (AVLTreeNode) rotateRight(tNode);
      System.out.println("DLR");
      assert isBalanced(temp) : "AVLTree not balanced";
      return temp;
   }

   protected AVLTreeNode singleRightRotation(AVLTreeNode tNode)
   {
      AVLTreeNode left = tNode.getLeft();

      tNode.setBalanceFactor(AVL.BALANCED);
      left.setBalanceFactor(AVL.BALANCED);

      AVLTreeNode temp = (AVLTreeNode) rotateRight(tNode);
      System.out.println("SR");
      assert isBalanced(temp) : "AVLTree not balanced";
      return temp;
   }
   protected AVLTreeNode singleLeftRotation(AVLTreeNode tNode)
   {
      AVLTreeNode right = tNode.getRight();

      tNode.setBalanceFactor(AVL.BALANCED);
      right.setBalanceFactor(AVL.BALANCED);

      AVLTreeNode temp = (AVLTreeNode) rotateLeft(tNode);
      System.out.println("SL");
      assert isBalanced(temp) : "AVLTree not balanced";
      return temp;
   }

}  
