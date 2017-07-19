
public class TreeNode 
{
   private Object item;
   private TreeNode leftChild;
   private TreeNode rightChild;

   public TreeNode(Object item) 
   {
      this.item = item;
      leftChild  = null;
      rightChild = null;
   } 

   public Object getItem() 
   {
      return item;
   }  

   public void setItem(Object item) 
   {
      this.item  = item;
   }  

   public TreeNode getLeft() 
   {
      return leftChild;
   } 

   public void setLeft(TreeNode left) 
   {
      leftChild  = left;
   }  

   public TreeNode getRight() 
   {
      return rightChild;
   }  

   public void setRight(TreeNode right) 
   {
      rightChild  = right;
   }  

} 