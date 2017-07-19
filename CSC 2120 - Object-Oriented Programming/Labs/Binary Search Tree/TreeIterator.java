import java.util.Iterator;

public interface TreeIterator extends Iterator
{
   //we can iterate in several different ways since trees are nonlinear
   public void setInorder();
   public void setPreorder();
   public void setPostorder();
}