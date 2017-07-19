enum AVL {LEFT_HEAVY, LEFT, BALANCED, RIGHT, RIGHT_HEAVY};

public class AVLTreeNode extends TreeNode
{
   private AVL balanceFactor;

   public AVLTreeNode(Object item) 
   {
      super(item);
      balanceFactor = AVL.BALANCED;
   }  

   public AVLTreeNode getLeft() 
   {
      return (AVLTreeNode) super.getLeft();
   } 

   public AVLTreeNode getRight() 
   {
      return (AVLTreeNode) super.getRight();
   } 

   public void setBalanceFactor(AVL avl)
   {
       balanceFactor = avl;
   }

   public AVL getBalanceFactor()
   {
       return balanceFactor;
   }

   public void insertLeft()
   {
      switch (balanceFactor)
      {
         case RIGHT:
            balanceFactor = AVL.BALANCED;
            break;
         case BALANCED:
            balanceFactor = AVL.LEFT;
            break;
         case LEFT:
            balanceFactor = AVL.LEFT_HEAVY;
            break;
      }
   }

   public void insertRight()
   {
      switch (balanceFactor)
      {
         case RIGHT:
            balanceFactor = AVL.RIGHT_HEAVY;
            break;
         case BALANCED:
            balanceFactor = AVL.RIGHT;
            break;
         case LEFT:
            balanceFactor = AVL.BALANCED;
            break;
      }
   }

   public void deleteLeft()
   {
      switch (balanceFactor)
      {
         case RIGHT:
            balanceFactor = AVL.RIGHT_HEAVY;
            break;
         case BALANCED:
            balanceFactor = AVL.RIGHT;
            break;
         case LEFT:
            balanceFactor = AVL.BALANCED;
            break;
      }
   }

   public void deleteRight()
   {
      switch (balanceFactor)
      {
         case RIGHT:
            balanceFactor = AVL.BALANCED;
            break;
         case BALANCED:
            balanceFactor = AVL.LEFT;
            break;
         case LEFT:
            balanceFactor = AVL.LEFT_HEAVY;
            break;
      }
   }

}  